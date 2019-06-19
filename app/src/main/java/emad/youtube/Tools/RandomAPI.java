package emad.youtube.Tools;

import java.util.ArrayList;
import java.util.Random;

public class RandomAPI {
     ArrayList randomAPIs = new ArrayList<>();

    public  void initKEYs(){
        randomAPIs.clear();
        randomAPIs.add("AIzaSyCo96-KRjALVjSCUAwhwwfxK09ZtjoqOgQ");
        randomAPIs.add("AIzaSyDYMLm9DLX-7616x4hqBRI-pWGZLIJN31w");
        randomAPIs.add("AIzaSyBIRZcTUGow6O3HkKh5mEZVDgNBpbYAQXU");
        randomAPIs.add("AIzaSyBhmTBLvxAMm_jAkFR5FsM2CO-rLGbv8j0");
        randomAPIs.add("AIzaSyD24VRxVAy7Z5gi2Z2MH2U_4tSXRxFPpmA");
        randomAPIs.add("AIzaSyD0JY82DZnXEsTm8Nv0KP3jNjFQaZO6d_I");
        randomAPIs.add("AIzaSyDQn2og-fhHc1JFgH2w5kKW1JHq6Thz8_M");
        randomAPIs.add("AIzaSyDy2ZZRgFBGNIiKNs-7o35TIoYeNX4kxEU");
        randomAPIs.add("AIzaSyB_sErFSK9dcnkWQcLHTsvXht8OUwiTdKA");
        randomAPIs.add("AIzaSyCg6o5HtWAqW9jSeveWZKn0VYE0Yb69iOI");
        randomAPIs.add("AIzaSyAdCw9TpAqumlfvtb_WOm3eXzJFkEiMtSU");
        randomAPIs.add("AIzaSyDFGlfW5s9kWb5iv0WI5hJElnsBubUO_yU");
    }

    public String getRandomKey(){

        initKEYs();
        Random rand = new Random();

        int n = rand.nextInt(12);
        return randomAPIs.get(n).toString();
    }
}
