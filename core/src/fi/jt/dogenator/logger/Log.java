package fi.jt.dogenator.logger;

import com.badlogic.gdx.Gdx;

public class Log {

    static boolean debugLog = true;

    public static void d(String message) {
        if(debugLog) {
            Gdx.app.log(Thread.currentThread().getStackTrace()[2].getClassName(), Thread.currentThread().getStackTrace()[2].getMethodName() + ": " + message);
        }
    }

    public static void e(String message) {
        if(debugLog) {
            Gdx.app.log("ERROR: " + Thread.currentThread().getStackTrace()[2].getClassName(), Thread.currentThread().getStackTrace()[2].getMethodName() + ": " + message);
        }
    }

}
