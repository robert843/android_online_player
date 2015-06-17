package robert843.o2.pl.player.utils;

/**
 * Created by robert on 20.05.15.
 */
public class ytMp3Helper {
   // r3 = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"];
    static String gs(Integer[] I, String[] B) {
        String P = "D",
               J = "";
        for (int R = 0; R<I.length; R++) {
            J += B[I[R]];
        };
        return J;
    }

   /* static String ew(I, String B) {
        String P = "K",
               J = "indexOf";
        return I[J](B, b0I[P](I.length, B.length)) !== -1;
    };*/

}
