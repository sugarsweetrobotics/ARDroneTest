// -*- Java -*-
/*!
 * @file  ARDroneTestImpl.java
 * @brief ARDrone Test RTC
 * @date  $Date$
 *
 * $Id$
 */

import RTC.CameraImage;
import RTC.TimedVelocity3D;
import RTC.TimedDouble;
import RTC.TimedOrientation3D;
import jp.go.aist.rtm.RTC.DataFlowComponentBase;
import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.port.InPort;
import jp.go.aist.rtm.RTC.port.OutPort;
import jp.go.aist.rtm.RTC.util.DataRef;
import jp.go.aist.rtm.RTC.port.CorbaConsumer;
import jp.go.aist.rtm.RTC.port.CorbaPort;
import RTC.ReturnCode_t;
import ssr.DroneService;

/*!
 * @class ARDroneTestImpl
 * @brief ARDrone Test RTC
 *
 */
public class ARDroneTestImpl extends DataFlowComponentBase {

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
        m_targetVelocity_val = new TimedVelocity3D();
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
        return super.onExecute(ec_id);
    }

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


}
