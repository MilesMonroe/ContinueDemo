package com.example.log_catcher.test_demo.test4_xml_json.json;

/**  GsonFormat插件的使用方法，作用:可根据已知的json报文生成对应的Bean
 *      1.Android studio  File->Settings..->Plugins-->Browse repositores..搜索GsonFormat
        2.安装插件,重启android studio
 *      3.类中右键Generate–》GsonFormat，将json样例拷贝进AS，点击ok即可
 */
public class test_json_bean {
    /**
     * mac : {"macAlg":"01","macData":"4573109BB20A03B1"}
     */

    private MacBean mac;

    public MacBean getMac() {
        return mac;
    }

    public void setMac(MacBean mac) {
        this.mac = mac;
    }

    public static class MacBean {
        /**
         * macAlg : 01
         * macData : 4573109BB20A03B1
         */

        private String macAlg;
        private String macData;

        public String getMacAlg() {
            return macAlg;
        }

        public void setMacAlg(String macAlg) {
            this.macAlg = macAlg;
        }

        public String getMacData() {
            return macData;
        }

        public void setMacData(String macData) {
            this.macData = macData;
        }
    }
}
