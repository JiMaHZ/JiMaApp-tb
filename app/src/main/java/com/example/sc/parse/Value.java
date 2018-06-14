package com.example.sc.parse;

public class Value {
    private String region;
    private String name;
    private CONFIG config;
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CONFIG getConfig() {
        return config;
    }

    public void setConfig(CONFIG config) {
        this.config = config;
    }

    public static class CONFIG {
        private String type;
        private String unit;
        private String control_addr;
        private String on;
        private String off;
        private String stop;
        private String cur_key;
        private String max_cur_key;
        private String on_cur_key;
        private String off_cur_key;
        private String max_on_cur_key;
        private String max_off_cur_key;

        public String getOff_cur_key() {
            return off_cur_key;
        }

        public void setOff_cur_key(String off_cur_key) {
            this.off_cur_key = off_cur_key;
        }

        public String getOn() {
            return on;
        }

        public void setOn(String on) {
            this.on = on;
        }

        public String getOff() {
            return off;
        }

        public void setOff(String off) {
            this.off = off;
        }

        public String getStop() {
            return stop;
        }

        public void setStop(String stop) {
            this.stop = stop;
        }

        public String getMax_on_cur_key() {
            return max_on_cur_key;
        }

        public void setMax_on_cur_key(String max_on_cur_key) {
            this.max_on_cur_key = max_on_cur_key;
        }

        public String getCur_key() {
            return cur_key;
        }

        public void setCur_key(String cur_key) {
            this.cur_key = cur_key;
        }

        public String getMax_cur_key() {
            return max_cur_key;
        }

        public void setMax_cur_key(String max_cur_key) {
            this.max_cur_key = max_cur_key;
        }

        public String getMax_off_cur_key() {
            return max_off_cur_key;
        }

        public void setMax_off_cur_key(String max_off_cur_key) {
            this.max_off_cur_key = max_off_cur_key;
        }

        public String getOn_cur_key() {
            return on_cur_key;
        }

        public void setOn_cur_key(String on_cur_key) {
            this.on_cur_key = on_cur_key;
        }

        public String getControl_addr() {
            return control_addr;
        }

        public void setControl_addr(String control_addr) {
            this.control_addr = control_addr;
        }


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }
}
