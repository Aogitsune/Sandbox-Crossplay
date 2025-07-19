package org.aogitsune.Class;

import java.util.List;
import java.util.ArrayList;

public class CrossplayDataClass {
    public List<Movement> Movements = new ArrayList<>();
    public List<Chat> Chats = new ArrayList<>();
    public List<Event> Events = new ArrayList<>();

    public static class Movement {
        public double x;
        public double y;
        public double z;
        public String n;
        public double t;
    }

    public static class Chat {
        public String p;   // optional: platform/user id
        public String m;   // message
        public String n;   // display name
        public long t;
        public String i;
    }

    public static class Event {
        public String n;   // message
        public String e;   // display name
        public long t;
    }
}