
package bgu.spl.mics.application;


import bgu.spl.mics.application.passiveObjects.Agent;

public class JsonToGson {
    public String[] inventory;
    public Services services;
    public Agent[] squad;


    public class Services {
        int M;
        int Moneypenny;
        public Intelligences[] intelligence;
        int time;

        public class Intelligences {
            public Missions[] missions;
        }

        public class Missions {
            public String[] serialAgentsNumbers;
            public int duration;
            public String gadget;
            public String name;
            public int timeExpired;
            public int timeIssued;
        }
    }

//        public void printToFile(Diary diary) {
//            try (FileWriter file = new FileWriter("/Users/nadavshaked/assignment2_spl/src/main/java/bgu/spl/mics/output.json")) {
//                Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                String json = gson.toJson(diary);
//                file.write(json);
//                System.out.println("Successfully Copied JSON Object to File...");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public class Squad {
        public String name;
        public String serialNumber;
    }
}
