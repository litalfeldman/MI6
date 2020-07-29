package bgu.spl.mics.application.passiveObjects;

import java.util.List;

/**
 * Passive data-object representing information about a mission.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class MissionInfo {

	private String MissionName;
	private List<String> SerialAgentsNumbers;
	private String Gadget;
	private int TimeIssued;
	private int TimeExpired;
	private int Duration;

	/**
     * Sets the name of the mission.
     */
    public void setMissionName(String missionName) {
        MissionName = missionName;
    }

	/**
     * Retrieves the name of the mission.
     */
	public String getMissionName() {
		return MissionName;
	}

    /**
     * Sets the serial agent number.
     */
    public void setSerialAgentsNumbers(List<String> serialAgentsNumbers) {
		SerialAgentsNumbers = serialAgentsNumbers;
    }

	/**
     * Retrieves the serial agent number.
     */
	public List<String> getSerialAgentsNumbers() {
		return SerialAgentsNumbers;
	}

    /**
     * Sets the gadget name.
     */
    public void setGadget(String gadget) {
    	Gadget = gadget;
    }

	/**
     * Retrieves the gadget name.
     */
	public String getGadget() {
		return Gadget;
	}

    /**
     * Sets the time the mission was issued in milliseconds.
     */
    public void setTimeIssued(int timeIssued) { //TODO: maybe something about miliseconds????
        TimeIssued = timeIssued;
    }

	/**
     * Retrieves the time the mission was issued in milliseconds.
     */
	public int getTimeIssued() {
		return TimeIssued;
	}

    /**
     * Sets the time that if it that time passed the mission should be aborted.
     */
    public void setTimeExpired(int timeExpired) {
        TimeExpired = timeExpired;
    }

	/**
     * Retrieves the time that if it that time passed the mission should be aborted.
     */
	public int getTimeExpired() {
		return TimeExpired;
	}

    /**
     * Sets the duration of the mission in time-ticks.
     */
    public void setDuration(int duration) {
        Duration = duration;
    }

	/**
	 * Retrieves the duration of the mission in time-ticks.
	 */
	public int getDuration() {
		return Duration;
	}
}
