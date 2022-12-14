import vecmath.Vec2f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by noahtell on 15-05-12.
 */
public class ToolField extends JToolBar {

    final static float DRAG_CONSTANT = 10f;


    private JButton heroButton;
    private JButton landscapeButton;

    private JButton listButton;
    private JButton clearButton;

    private JButton deleteButton;
    private JButton imageDragButton;
    private JButton imageGravityButton;

    private JButton movementButton;

    private JButton paintButton;
    private JButton eraseButton;

    private JButton lastClicked;
    private JButton debugButton;


    private MouseListener currentMouseListener;
    private MouseAdapter currentClickMotionListener;
    private MouseMotionListener currentMouseMotionListener;
    private final GUI gui;

    public ToolField(final GUI gui) {
        super();
        this.gui = gui;

        //Reset Hero
        heroButton = new JButton("Reset Hero");
        heroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.world.recreateHero();
            }
        });
        add(heroButton);

        //Reset Hero
        landscapeButton = new JButton("Reset Landscape");
        landscapeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.world.recreateLandscape();
            }
        });
        add(landscapeButton);


        //Clear images
        clearButton = new JButton("Clear Images");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.world.clearImages();
            }
        });
        add(clearButton);

        add(new JLabel(" Actions: "));


        //turns on and off list panel visibilty
        listButton = new JButton("Add Image");
        listButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gui.listPanel.isVisible() == false){
                    gui.listPanel.setVisible(true);
                }else {
                    gui.listPanel.setVisible(false);
                }
            }
        });
        add(listButton);


        //Makes drag delete imageObjects
        deleteButton = new JButton("Delete Image");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastClicked = deleteButton;

                removeListeners();

                currentClickMotionListener = new MouseAdapter()
                {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ImageObject clickedObject = gui.world.getFromScreenCoord(e.getX(), e.getY());
                        if(clickedObject != null)
                            gui.world.removeImage(clickedObject);
                    }
                    @Override
                    public void mousePressed(MouseEvent e) {
                        ImageObject clickedObject = gui.world.getFromScreenCoord(e.getX(), e.getY());
                        if(clickedObject != null)
                            gui.world.removeImage(clickedObject);
                    }
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        ImageObject clickedObject = gui.world.getFromScreenCoord(e.getX(), e.getY());
                        if(clickedObject != null)
                            gui.world.removeImage(clickedObject);
                    }
                };
                gui.world.addMouseListener(currentClickMotionListener);
                gui.world.addMouseMotionListener(currentClickMotionListener);
            }
        });
        add(deleteButton);

        //makes the pointer drag a velocity from imageObjects
        imageDragButton = new JButton("Drag Image");
        imageDragButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastClicked = imageDragButton;
                removeListeners();
               // System.out.println("CLICK");
                currentClickMotionListener = new MouseAdapter() {
                    Vec2f currentImageClickPoint = new Vec2f();
                    Vec2f mouseWorldCoord = new Vec2f();

                    ImageForce imageForce = new ImageForce(null, null){ //here we threat force as mouse world chords, like where we want to get.
                        @Override
                        public void apply(float deltaTime){
                            if(getImg() != null)
                                ImagePhysics.accelerate(getImg(), Vec2f.sub(mouseWorldCoord, Vec2f.add(currentImageClickPoint, imageForce.getImg().getPosition())).mult(DRAG_CONSTANT), deltaTime);
                        }
                    };
                    @Override
                    public void mouseClicked(MouseEvent e) {
                            mousePressed(e);
                    }
                    @Override
                    public void mousePressed(MouseEvent e) {
                        mouseWorldCoord.set(gui.world.xScreenToWorld(e.getX()), gui.world.yScreenToWorld(e.getY()));
                        imageForce.setImg(gui.world.getFromScreenCoord(e.getX(), e.getY()));
                        if(imageForce.getImg() != null){
                            currentImageClickPoint = Vec2f.sub(mouseWorldCoord, imageForce.getImg().getPosition());
                            gui.world.addForce(imageForce);
                        }
                    }
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        gui.world.removeForce(imageForce);
                    }
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        mouseWorldCoord.set(gui.world.xScreenToWorld(e.getX()), gui.world.yScreenToWorld(e.getY()));
                    }
                };

                gui.world.addMouseListener(currentClickMotionListener);
                gui.world.addMouseMotionListener(currentClickMotionListener);

            }
        });
        add(imageDragButton);

        //makes the pointer drag a velocity from imageObjects
        imageGravityButton = new JButton("Attract Images");
        imageGravityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastClicked = imageDragButton;
                removeListeners();
                // System.out.println("CLICK");
                currentClickMotionListener = new MouseAdapter() {
                    Vec2f mouseWorldCoord = new Vec2f();

                    ImageGlobalForce imageGlobalForce = new ImageGlobalForce(null) { //here we threat force as mouse world chords, like where we want to get.
                        @Override
                        public void apply(ImageObject img, float deltaTime) {
                            if (img != null)
                                ImagePhysics.accelerate(img, Vec2f.sub(mouseWorldCoord, img.getPosition()).mult(DRAG_CONSTANT), deltaTime); //Stronger the further...
                        }
                    };

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mousePressed(e);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        mouseWorldCoord.set(gui.world.xScreenToWorld(e.getX()), gui.world.yScreenToWorld(e.getY()));
                        gui.world.addGlobalForce(imageGlobalForce);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        gui.world.removeGlobalForce(imageGlobalForce);
                    }

                    @Override
                    public void mouseDragged(MouseEvent e) {
                        mouseWorldCoord.set(gui.world.xScreenToWorld(e.getX()), gui.world.yScreenToWorld(e.getY()));
                    }
                };

                gui.world.addMouseListener(currentClickMotionListener);
                gui.world.addMouseMotionListener(currentClickMotionListener);

            }
        });
        add(imageGravityButton);


        //Paint images with black.
        paintButton = new JButton("Paint");
        paintButton.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  lastClicked = paintButton;
                  removeListeners();
                  // System.out.println("CLICK");
                  currentClickMotionListener = new MouseAdapter() {
                      ImageObject target = null;
                      Color color = new Color(0, 0, 0, 255);

                      @Override
                      public void mouseClicked(MouseEvent e) {
                          mousePressed(e);
                      }

                      @Override
                      public void mousePressed(MouseEvent e) {
                          int mouseWorldX = (int)(gui.world.xScreenToWorld(e.getX()));
                          int mouseWorldY = (int)(gui.world.yScreenToWorld(e.getY()));
                          // TODO: The feature of painting on anything is nice if the images would expand to fit the line.
                          //target = gui.world.getFromScreenCoord(e.getX(), e.getY());
                          if(target == null)
                              target = gui.world.landscape;
                          if(target != null)
                            target.setGlobalRGB(mouseWorldX, mouseWorldY, color.getRGB());
                      }

                      @Override
                      public void mouseReleased(MouseEvent e) {
                          target = null;
                      }

                      @Override
                      public void mouseDragged(MouseEvent e) {
                          if(target != null) {
                              int mouseWorldX = (int)(gui.world.xScreenToWorld(e.getX()));
                              int mouseWorldY = (int)(gui.world.yScreenToWorld(e.getY()));
                              target.setGlobalRGB(mouseWorldX, mouseWorldY, color.getRGB());
                              target.calcCenterAndMass();
                          }
                      }
                  };

                  gui.world.addMouseListener(currentClickMotionListener);
                  gui.world.addMouseMotionListener(currentClickMotionListener);
              }
        });
        add(paintButton);

        //Paint images with 0.
        eraseButton = new JButton("Eraser");
        eraseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastClicked = eraseButton;
                removeListeners();
                int r = 2;
                // System.out.println("CLICK");
                currentClickMotionListener = new MouseAdapter() {
                    ImageObject target = null;

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mousePressed(e);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        int mouseWorldX = (int)(gui.world.xScreenToWorld(e.getX()));
                        int mouseWorldY = (int)(gui.world.yScreenToWorld(e.getY()));
                        target =  gui.world.getFromScreenCoord(e.getX(), e.getY());
                        if(target != null) {
                            for(int x = mouseWorldX-r; x <= mouseWorldX+r; ++x)
                                for(int y = mouseWorldY-r; y <= mouseWorldY+r; ++y)
                                    target.setGlobalRGB(x, y, 0);
                            target.calcCenterAndMass();
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        target = null;
                    }

                    @Override
                    public void mouseDragged(MouseEvent e) {
                        target = gui.world.getFromScreenCoord(e.getX(), e.getY());
                        if(target != null){
                            int mouseWorldX = (int)(gui.world.xScreenToWorld(e.getX()));
                            int mouseWorldY = (int)(gui.world.yScreenToWorld(e.getY()));
                            for(int x = mouseWorldX-r; x <= mouseWorldX+r; ++x)
                                for(int y = mouseWorldY-r; y <= mouseWorldY+r; ++y)
                                    target.setGlobalRGB(x, y, 0);
                            target.calcCenterAndMass();
                        }
                    }
                };

                gui.world.addMouseListener(currentClickMotionListener);
                gui.world.addMouseMotionListener(currentClickMotionListener);
            }
        });
        add(eraseButton);


        //Gives info on mouse image
        debugButton = new JButton("Debug Image");
        debugButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastClicked = debugButton;

                removeListeners();

                currentMouseListener = new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        ImageObject image = gui.world.getFromScreenCoord(e.getX(),e.getY());
                        if (image != null){
                            System.out.println(image.toString());
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                };

                gui.world.addMouseListener(currentMouseListener);
            }
        });
        add(debugButton);


    }

    private void removeListeners(){
        gui.world.removeMouseMotionListener(currentMouseMotionListener);
        gui.world.removeMouseListener(currentClickMotionListener);
        gui.world.removeMouseMotionListener(currentClickMotionListener);
        gui.world.removeMouseListener(currentMouseListener);
    }
}
