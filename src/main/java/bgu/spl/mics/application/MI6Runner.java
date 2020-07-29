package bgu.spl.mics.application;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static CountDownLatch countDownLatch;

    public static void main(String[] args) throws FileNotFoundException {

        Gson gson = new Gson();
        JsonReader jr = new JsonReader(new FileReader(args[0]));
        Inventory inventory = Inventory.getInstance();
        Squad squad = Squad.getInstance();
        JsonToGson data = gson.fromJson(jr, JsonToGson.class);
        inventory.load(data.inventory);
        squad.load(data.squad);
        List<Subscriber> subscribers = new LinkedList<>();
        try {
            int Duration = data.services.time;
            inventory.load(data.inventory);
            squad.load(data.squad);
            int numOfM = data.services.M;
            int numOfMoneyPenny = data.services.Moneypenny;
            JsonToGson.Services.Intelligences[] intelligences = data.services.intelligence;

            for (int i = 1; i <= numOfM; i++) {
                M m = new M(i);
                subscribers.add(m);

            }
            for (int i = 1; i <= numOfMoneyPenny; i++) {
                Moneypenny moneypenny = new Moneypenny(i);
                subscribers.add(moneypenny);
            }
            int idCounter = 0;
            for (JsonToGson.Services.Intelligences gsonIntel : intelligences) {
                List<MissionInfo> missionInfoList = new LinkedList<>();
                for (JsonToGson.Services.Missions mission : gsonIntel.missions) {
                    MissionInfo mi = new MissionInfo();
                    mi.setDuration(mission.duration);
                    mi.setTimeIssued(mission.timeIssued);
                    mi.setTimeExpired(mission.timeExpired);
                    mi.setSerialAgentsNumbers(Arrays.asList(mission.serialAgentsNumbers));
                    mi.setMissionName(mission.name);
                    mi.setGadget(mission.gadget);
                    missionInfoList.add(mi);
                }
                Intelligence intelligence = new Intelligence(idCounter, missionInfoList);
                subscribers.add(intelligence);
                idCounter++;
            }
            int h = 0;
            List<Thread> threads = new LinkedList<>();
            for (Subscriber subscriber : subscribers)
                threads.add(new Thread(subscriber, subscriber.getName()));
            threads.add(new Thread(new Q()));
            countDownLatch = new CountDownLatch(threads.size());
            for (Thread thread1 : threads)
                thread1.start();
            countDownLatch.await();

            Thread timeThread = new Thread(new TimeService(data.services.time), "CLOCK");
            timeThread.start();
            timeThread.join();

            for (Thread thread : threads)
                thread.join();

            Inventory.getInstance().printToFile(args[1]);
            Diary.getInstance().printToFile(args[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//
//
//        Q q = new Q(1);
//        Thread qThread = new Thread(q);
//        qThread.start();
//
//        Thread timeService = new Thread(new TimeService(appDuration));
//        timeService.start();
//        } catch (FileNotFoundException e) {
//        e.printStackTrace();
//        }
//        }
//
//private static List<MissionInfo> convertFromJsonParserToMissionInfoList(JsonParser.MI6Class.IntelligencesArray missionsArray) {
//        List<MissionInfo> missionInfoList = new LinkedList<>();
//        for (JsonParser.MI6Class.Missions mission : missionsArray.missions) {
//        MissionInfo missionInfo = new MissionInfo();
//        missionInfo.setMissionName(mission.name);
//        List<String> serialAgentsNumbers = new LinkedList<>();
//        for (String serialAgentsNumber : mission.serialAgentsNumbers) {
//        serialAgentsNumbers.add(serialAgentsNumber);
//        }
//        missionInfo.setSerialAgentsNumbers(serialAgentsNumbers);
//        missionInfo.setGadget(mission.gadget);
//        missionInfo.setTimeIssued(mission.timeIssued);
//        missionInfo.setTimeExpired(mission.timeExpired);
//        missionInfo.setDuration(mission.duration);
//
//        missionInfoList.add(missionInfo);
//        }
//        return missionInfoList;
//        }

