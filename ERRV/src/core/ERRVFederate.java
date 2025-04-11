package core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;
import java.util.Random;

import model.ERRV;
import model.interactionclass.EmergencyRequest;
import model.interactionclass.WASSERRVMessage;
import model.Objecttoget;
import model.Parts;
import model.Position;
import model.interactionclass.WASSERRVMessage;
import siso.smackdown.frame.FrameType;
import skf.config.Configuration;
import skf.core.SEEAbstractFederate;
import skf.core.SEEAbstractFederateAmbassador;
import skf.model.interaction.annotations.InteractionClass;
import skf.model.interaction.modeTransitionRequest.ModeTransitionRequest;
import skf.model.object.annotations.ObjectClass;
import skf.model.object.executionConfiguration.ExecutionConfiguration;
import skf.model.object.executionConfiguration.ExecutionMode;
import skf.synchronizationPoint.SynchronizationPoint;

public class ERRVFederate extends SEEAbstractFederate implements Observer {

	private static final int MAX_WAIT_TIME = 10000;
	// DEFINITION OF THE OBJECT TO DELIVER
	private Objecttoget objectToDeliver = null;
	// DEFINITION OF THE LIST OF OBJECTS TO DELIVER
	private WASSERRVMessage message = new WASSERRVMessage();
	List<String> listOfObjectToDeliver = null;
	private ERRV lunarRover = null;
	private String local_settings_designator = null;
	private ModeTransitionRequest mtr = null;
	private double distance;
	private double distanceActive;
	private double theta;
	private double phy;
	private double delivering = 0.0;
	private SimpleDateFormat format = null;

	public ERRVFederate(SEEAbstractFederateAmbassador seefedamb, ERRV lunarRover) {
		super(seefedamb);
		this.lunarRover = lunarRover;
		this.mtr = new ModeTransitionRequest();
		this.listOfObjectToDeliver = new ArrayList<>();

		this.format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@SuppressWarnings("unchecked")
	public void configureAndStart(Configuration config) throws Exception {
		// 1. configure the SKF framework
		super.configure(config);

		// 2. Connect on RTI
		/*
		 * For MAK local_settings_designator = ""; For PITCH local_settings_designator =
		 * "crcHost=" + <crc_host> + "\ncrcPort=" + <crc_port>;
		 */
		local_settings_designator = "crcHost=" + config.getCrcHost() + "\ncrcPort=" + config.getCrcPort();
		super.connectToRTI(local_settings_designator);

		// 3. join the SEE Federation execution
		super.joinFederationExecution();

		// 4. Subscribe the Subject
		super.subscribeSubject(this);

		/*
		 * 5. Check if the federate is a Late Joiner Federate. All the Federates of the
		 * SEE Teams must be Late Joiner.
		 */
		if (!SynchronizationPoint.INITIALIZATION_STARTED.isAnnounced()) {

			// 6. Wait for the announcement of the Synch-Point "initialization_completed"
			super.waitingForAnnouncement(SynchronizationPoint.INITIALIZATION_COMPLETED, MAX_WAIT_TIME);

			/*
			 * 7. Wait for announcement of "objects_discovered", and Federation Specific
			 * Mutiphase Initialization Synch-Points
			 */
			// -> skipped

			/*
			 * 8. Subscribe Execution Control Object Class Attributes and wait for ExCO
			 * Discovery
			 */
			super.subscribeElement((Class<? extends ObjectClass>) ExecutionConfiguration.class);
			super.waitForElementDiscovery((Class<? extends ObjectClass>) ExecutionConfiguration.class, MAX_WAIT_TIME);

			// 9. Request ExCO update
			while (super.getExecutionConfiguration() == null) {
				super.requestAttributeValueUpdate((Class<? extends ObjectClass>) ExecutionConfiguration.class);
				Thread.sleep(10);
			}

			// 10. Publish MTR Interaction
			super.publishInteraction(this.mtr);
			super.publishInteraction(message);

			try {
				super.subscribeInteraction((Class<? extends InteractionClass>) EmergencyRequest.class);
				super.subscribeInteraction((Class<? extends InteractionClass>) WASSERRVMessage.class);

			} catch (Exception e) {

				e.printStackTrace();
			}

			/*
			 * 11. Publish and Subscribe Object Class Attributes and Interaction Class
			 * Parameters
			 *
			 * 12. Reserve All Federate Object Instance Names
			 *
			 * 13. Wait for All Federate Object Instance Name Reservation Success/Failure
			 * Callbacks
			 *
			 * 14. Register Federate Object Instances
			 */
			super.publishElement(lunarRover, "ERRV");
			super.subscribeReferenceFrame(FrameType.AitkenBasinLocalFixed);

			// 15. Wait for All Required Objects to be Discovered
			// -> Skipped

			/*
			 * 16. Setup HLA Time Management and Query GALT, Compute HLTB and Time Advance
			 * to HLTB
			 */
			super.setupHLATimeManagement();

			// 17. Achieve "objects_discovered" Sync-Point and wait for synchronization
			// -> Skipped
		} else {
			throw new RuntimeException("The federate " + config.getFederateName() + "is not a Late Joiner Federate");
		}

		// 18. Start simulation execution
		super.startExecution();

	}

	@Override
	protected void doAction() {
		double X2, Y2, Z2;

		// Step 1: Assign object if available
		if (this.lunarRover.getAvailability() && !this.listOfObjectToDeliver.isEmpty()) {
			this.objectToDeliver = new Objecttoget(this.listOfObjectToDeliver.remove(0));

			// Weight check: skip if too heavy
			if (objectToDeliver.getWeight() > lunarRover.getArm().getMaxCapacity()) {
				System.out.println("[ERRV] Object too heavy: " + objectToDeliver.getWeight() + "kg");
				this.objectToDeliver = null;
				return;
			}

			this.lunarRover.setAvailability(false);
			X2 = objectToDeliver.getX() - lunarRover.getPosition().getX();
			Y2 = objectToDeliver.getY() - lunarRover.getPosition().getY();
			Z2 = objectToDeliver.getZ() - lunarRover.getPosition().getZ();
			this.distance = Math.sqrt(X2 * X2 + Y2 * Y2 + Z2 * Z2);

			System.out.println("[ERRV] Distance to object: " + distance);
			this.theta = Math.acos(Z2 / distance);
			this.phy = Math.acos(X2 / (distance * Math.sin(theta)));
		}

		// Step 2: ERRV in motion
		if (!this.lunarRover.getAvailability() && this.objectToDeliver != null) {
			X2 = objectToDeliver.getX() - lunarRover.getPosition().getX();
			Y2 = objectToDeliver.getY() - lunarRover.getPosition().getY();
			Z2 = objectToDeliver.getZ() - lunarRover.getPosition().getZ();
			this.distanceActive = Math.sqrt(X2 * X2 + Y2 * Y2 + Z2 * Z2);

			if (distanceActive > 11.0) {
				// Move toward object
				Position curr_pos = lunarRover.getPosition();
				try {
					curr_pos.setX(curr_pos.getX() + lunarRover.getVelocity() * Math.cos(phy) * Math.sin(theta));
					curr_pos.setY(curr_pos.getY() + lunarRover.getVelocity() * Math.sin(phy) * Math.sin(theta));
					curr_pos.setZ(curr_pos.getZ() + lunarRover.getVelocity() * Math.cos(theta));
					System.out.println("[ERRV] Moving toward object... Distance: " + distanceActive);
					super.updateElement(lunarRover);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// Within reach: operate the arm
				if (delivering < 100.0) {
					System.out.println("[ERRV] In position. Arm picking up object... " + delivering + "%");
					delivering += 10.0;
				} else {
					System.out.println("[ERRV] Object pickup complete.");
					int index = new Random().nextInt(lunarRover.sendParts().size());
					Parts part = lunarRover.sendParts().get(index);
					sendMessage("ERRV", "WASS", "PART", part.toString());
					delivering = 0.0;
					lunarRover.setAvailability(true);
					this.objectToDeliver = null;
				}
			}
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {

		// System.out.println("ERRV has received an update.");
		if (arg1 instanceof ExecutionConfiguration) {
			super.setExecutionConfiguration((ExecutionConfiguration) arg1);

			/* Manage state transitions */
			if (super.getExecutionConfiguration().getCurrent_execution_mode() == ExecutionMode.EXEC_MODE_RUNNING
					&& super.getExecutionConfiguration().getNext_execution_mode() == ExecutionMode.EXEC_MODE_FREEZE) {
				super.freezeExecution();
			}

			else if (super.getExecutionConfiguration().getCurrent_execution_mode() == ExecutionMode.EXEC_MODE_FREEZE
					&& super.getExecutionConfiguration().getNext_execution_mode() == ExecutionMode.EXEC_MODE_RUNNING) {
				super.resumeExecution();
			}

			else if ((super.getExecutionConfiguration().getCurrent_execution_mode() == ExecutionMode.EXEC_MODE_FREEZE
					|| super.getExecutionConfiguration().getCurrent_execution_mode() == ExecutionMode.EXEC_MODE_RUNNING)
					&& super.getExecutionConfiguration().getNext_execution_mode() == ExecutionMode.EXEC_MODE_SHUTDOWN) {
				super.shudownExecution();
			} else {
				// System.out.println("ExecutionConfiguration status unknown");
				/* End Manage state transitions */
			}
		} else if (arg1 instanceof EmergencyRequest) {
			System.out.println(((EmergencyRequest) arg1).getRequest());

			this.listOfObjectToDeliver.add(((EmergencyRequest) arg1).getRequest());
			// }
		} else if (arg1 instanceof WASSERRVMessage) {
			WASSERRVMessage msg = (WASSERRVMessage) arg1;
			System.out.println("[ERRV] Received message from WASS:" + msg.getMessageType() + " :\n" + msg.getContent());
		} else {
			System.out.println("unknown type");
		}

	}

	public void getPosition() {
		System.out.println("The position of ERRV  is : " + this.lunarRover.getPosition());
	}

	private void sendMessage(String sender, String receiver, String type, String content) {
		this.message.setSender(sender);
		this.message.setReceiver(receiver);
		this.message.setMessageType(type);
		this.message.setContent(content);

		try {
			super.updateInteraction(this.message);
			System.out.println("[ERRV] Sent message to " + receiver + ":\n" + content);
		} catch (Exception e) {
			System.out.println("[ERRV] Failed to send message: " + e.getMessage());
		}
	}
}
