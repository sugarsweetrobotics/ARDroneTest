// -*- Java -*-
/*!
 * @file  ARDroneTestImpl.java
 * @brief ARDrone Test RTC
 * @date  $Date$
 *
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jp.go.aist.rtm.RTC.DataFlowComponentBase;
import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.port.CorbaConsumer;
import jp.go.aist.rtm.RTC.port.CorbaPort;
import jp.go.aist.rtm.RTC.port.InPort;
import jp.go.aist.rtm.RTC.port.OutPort;
import jp.go.aist.rtm.RTC.util.DataRef;
import ssr.DroneService;
import RTC.CameraImage;
import RTC.ReturnCode_t;
import RTC.TimedDouble;
import RTC.TimedOrientation3D;
import RTC.TimedVelocity3D;

/*!
 * @class ARDroneTestImpl
 * @brief ARDrone Test RTC
 *
 */
public class ARDroneTestImpl extends DataFlowComponentBase {

  private DronePane panel;

	/*!
   * @brief constructor
   * @param manager Maneger Object
   */
	public ARDroneTestImpl(Manager manager) {  
        super(manager);
        // <rtc-template block="initializer">
        m_camera_val = new CameraImage();
        m_camera = new DataRef<CameraImage>(m_camera_val);
        m_cameraIn = new InPort<CameraImage>("camera", m_camera);
        m_currentVelocity_val = new TimedVelocity3D();
        m_currentVelocity = new DataRef<TimedVelocity3D>(m_currentVelocity_val);
        m_currentVelocityIn = new InPort<TimedVelocity3D>("currentVelocity", m_currentVelocity);
        m_attitude_val = new TimedDouble();
        m_attitude = new DataRef<TimedDouble>(m_attitude_val);
        m_attitudeIn = new InPort<TimedDouble>("attitude", m_attitude);
        m_orientation_val = new TimedOrientation3D();
        m_orientation = new DataRef<TimedOrientation3D>(m_orientation_val);
        m_orientationIn = new InPort<TimedOrientation3D>("orientation", m_orientation);
        m_targetVelocity_val = new TimedVelocity3D(new RTC.Time(0,0), new RTC.Velocity3D(0, 0, 0, 0, 0, 0));
        m_targetVelocity = new DataRef<TimedVelocity3D>(m_targetVelocity_val);
        m_targetVelocityOut = new OutPort<TimedVelocity3D>("targetVelocity", m_targetVelocity);
        m_DroneServicePort = new CorbaPort("DroneService");
        // </rtc-template>

    }

    /**
     *
     * The initialize action (on CREATED->ALIVE transition)
     * formaer rtc_init_entry() 
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
    @Override
    protected ReturnCode_t onInitialize() {
        // Registration: InPort/OutPort/Service
        // <rtc-template block="registration">
        // Set InPort buffers
        addInPort("camera", m_cameraIn);
        addInPort("currentVelocity", m_currentVelocityIn);
        addInPort("attitude", m_attitudeIn);
        addInPort("orientation", m_orientationIn);
        
        // Set OutPort buffer
        addOutPort("targetVelocity", m_targetVelocityOut);
        
        // Set service consumers to Ports
        m_DroneServicePort.registerConsumer("DroneService", "ssr.DroneService", m_droneServiceBase);
        
        // Set CORBA Service Ports
        addPort(m_DroneServicePort);
        // </rtc-template>
        gui = new JFrame();
    	gui.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				int code = e.getKeyCode();
				switch(code) {
				case KeyEvent.VK_T:
					takeOff();
					break;
				case KeyEvent.VK_L:
					landing();
					break;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
					int code = e.getKeyCode();
					switch(code) {
					case KeyEvent.VK_T:
						takeOff();
						break;
					case KeyEvent.VK_L:
						landing();
						break;
						
					case KeyEvent.VK_RIGHT:
						move3D(0, 0, 0, -10);
						break;
					case KeyEvent.VK_LEFT:
						move3D(0, 0, 0, 10);
						break;
					case KeyEvent.VK_UP:
						move3D(10, 0, 0, 0);
						break;
					case KeyEvent.VK_DOWN:
						move3D(-10, 0, 0, 0);
						break;
					}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
    	gui.setSize(800, 600);

    	return super.onInitialize();
    }

    /***
     *
     * The finalize action (on ALIVE->END transition)
     * formaer rtc_exiting_entry()
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onFinalize() {
//        return super.onFinalize();
//    }

    /***
     *
     * The startup action when ExecutionContext startup
     * former rtc_starting_entry()
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onStartup(int ec_id) {
//        return super.onStartup(ec_id);
//    }

    /***
     *
     * The shutdown action when ExecutionContext stop
     * former rtc_stopping_entry()
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onShutdown(int ec_id) {
//        return super.onShutdown(ec_id);
//    }

    /***
     *
     * The activated action (Active state entry action)
     * former rtc_active_entry()
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
    @Override
    protected ReturnCode_t onActivated(int ec_id) {
        
    	
    	this.panel = new DronePane(this);
    	gui.getContentPane().add(panel, BorderLayout.CENTER);
    	gui.setVisible(true);
        return super.onActivated(ec_id);
    }

    /***
     *
     * The deactivated action (Active state exit action)
     * former rtc_active_exit()
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
    @Override
    protected ReturnCode_t onDeactivated(int ec_id) {
    	gui.setVisible(false);
        return super.onDeactivated(ec_id);
    }

    /***
     *
     * The execution action that is invoked periodically
     * former rtc_active_do()
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
    @Override
    protected ReturnCode_t onExecute(int ec_id) {
    	if (this.m_cameraIn.isNew()) {
    		m_cameraIn.read();
    		//System.out.println("Width  =" + m_in.v.width);
    		//System.out.println("Height =" + m_in.v.height);
    		if (img == null) {
    			img = new BufferedImage(m_camera.v.width, m_camera.v.height, BufferedImage.TYPE_INT_RGB);
    		}
    		for (int y = 0;y < m_camera.v.height;y++) {
    			for (int x = 0;x < m_camera.v.width;x++) {
    				int index = y * m_camera.v.width + x;
    				byte r = m_camera.v.pixels[index*3 + 2];
    				byte g = m_camera.v.pixels[index*3 + 1];
    				byte b = m_camera.v.pixels[index*3 + 0];
    				int rgb = (0x00FF & (int)r) << 16 | (0x00FF & (int)g) << 8 | (0x00FF & (int)b);
    				img.setRGB(x, y, rgb);
    			}
    		}
    		panel.img = img;
    		panel.repaint();
    	}
    	
        return super.onExecute(ec_id);
    }

    private BufferedImage img;
    /***
     *
     * The aborting action when main logic error occurred.
     * former rtc_aborting_entry()
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//  @Override
//  public ReturnCode_t onAborting(int ec_id) {
//      return super.onAborting(ec_id);
//  }

    /***
     *
     * The error action in ERROR state
     * former rtc_error_do()
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    public ReturnCode_t onError(int ec_id) {
//        return super.onError(ec_id);
//    }

    /***
     *
     * The reset action that is invoked resetting
     * This is same but different the former rtc_init_entry()
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
    @Override
    protected ReturnCode_t onReset(int ec_id) {
        return super.onReset(ec_id);
    }

    /***
     *
     * The state update action that is invoked after onExecute() action
     * no corresponding operation exists in OpenRTm-aist-0.2.0
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onStateUpdate(int ec_id) {
//        return super.onStateUpdate(ec_id);
//    }

    /***
     *
     * The action that is invoked when execution context's rate is changed
     * no corresponding operation exists in OpenRTm-aist-0.2.0
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onRateChanged(int ec_id) {
//        return super.onRateChanged(ec_id);
//    }
//
    // DataInPort declaration
    // <rtc-template block="inport_declare">
    protected CameraImage m_camera_val;
    protected DataRef<CameraImage> m_camera;
    /*!
     */
    protected InPort<CameraImage> m_cameraIn;

    protected TimedVelocity3D m_currentVelocity_val;
    protected DataRef<TimedVelocity3D> m_currentVelocity;
    /*!
     */
    protected InPort<TimedVelocity3D> m_currentVelocityIn;

    protected TimedDouble m_attitude_val;
    protected DataRef<TimedDouble> m_attitude;
    /*!
     */
    protected InPort<TimedDouble> m_attitudeIn;

    protected TimedOrientation3D m_orientation_val;
    protected DataRef<TimedOrientation3D> m_orientation;
    /*!
     */
    protected InPort<TimedOrientation3D> m_orientationIn;

    
    // </rtc-template>

    // DataOutPort declaration
    // <rtc-template block="outport_declare">
    protected TimedVelocity3D m_targetVelocity_val;
    protected DataRef<TimedVelocity3D> m_targetVelocity;
    /*!
     */
    protected OutPort<TimedVelocity3D> m_targetVelocityOut;

    
    // </rtc-template>

    // CORBA Port declaration
    // <rtc-template block="corbaport_declare">
    /*!
     */
    protected CorbaPort m_DroneServicePort;
    
    // </rtc-template>

    // Service declaration
    // <rtc-template block="service_declare">
    
    // </rtc-template>

    // Consumer declaration
    // <rtc-template block="consumer_declare">
    protected CorbaConsumer<DroneService> m_droneServiceBase = new CorbaConsumer<DroneService>(DroneService.class);
    /*!
     */
    protected DroneService m_droneService;
    
    // </rtc-template>

    JFrame gui;
    
    private boolean flight;
    public boolean isFlight() {return flight;}
    public void toggleFlight() {
    	if (m_DroneServicePort.getPortProfile().connector_profiles.length > 0) {
    		

    	if (flight) {
    		m_droneServiceBase._ptr().landing();
    		flight = false;
    	} else {
    		m_droneServiceBase._ptr().takeOff();
//    		m_droneService.takeOff();
    		flight = true;
    	}
    	}
    }
    
    public void landing() {
    	if (m_DroneServicePort.getPortProfile().connector_profiles.length > 0) {
    		m_droneServiceBase._ptr().landing();
    	}
    }
    
    public void takeOff() {
    	if (m_DroneServicePort.getPortProfile().connector_profiles.length > 0) {
    		m_droneServiceBase._ptr().takeOff();
    	}
    }
    
    public void move3D(double x, double y, double z, double yaw) {
		m_targetVelocity.v.data.vx = x;
		m_targetVelocity.v.data.vy = y;
		m_targetVelocity.v.data.vz = z;
		m_targetVelocity.v.data.va = yaw;
		m_targetVelocityOut.write();
    }
}

class DronePane extends JPanel {

	public BufferedImage img;
	GridBagConstraints  gbc;
	GridBagLayout  layout;
	public void add(int gx, int gy, int gw, int gh, double ww, double wh, JComponent c) {
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.ipadx = 5;
		gbc.ipady = 5;
		gbc.weightx = ww;
		gbc.weighty = wh;
		gbc.fill = GridBagConstraints.BOTH;
		layout.setConstraints(c, gbc);
		add(c);
	}
	ARDroneTestImpl rtc;
	
	public DronePane(ARDroneTestImpl arDroneTestImpl) {
		// TODO Auto-generated constructor stub
		super();
		rtc = arDroneTestImpl;
		layout = new GridBagLayout();
		setLayout(layout);
		gbc = new GridBagConstraints();
		img = new BufferedImage(640, 360, BufferedImage.TYPE_3BYTE_BGR);
		
		setSize(680, 480);
		setBounds(0, 0, 640, 360);
		JPanel imgPane = new JPanel() {
			@Override
			public void paint(Graphics g) {
				if (img != null) {
					g.drawImage(img, 0, 0, this);
				}
			}
		};
		
		
		imgPane.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				//rtc.landing();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		add(0, 0, 2, 1, 1.0, 1.0, imgPane);
		add(0, 1, 1, 1, 1.0, 0, new JLabel("Roll"));
		JTextField t1 = new JTextField("0.0");
		t1.setFocusable(false);
		t1.setEnabled(false);
		add(1, 1, 1, 1, 1.0, 0, t1);
		add(0, 2, 1, 1, 1.0, 0, new JLabel("Yaw"));
		JTextField t2 = new JTextField("0.0");
		t2.setFocusable(false);
		t2.setEnabled(false);
		add(1, 2, 1, 1, 1.0, 0, t2);
		add(0, 3, 1, 1, 1.0, 0, new JLabel("Pitch"));
		JTextField t3 = new JTextField("0.0");
		t3.setFocusable(false);
		t3.setEnabled(false);
		add(1, 3, 1, 1, 1.0, 0, t3);
	}
	
}
