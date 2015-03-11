package com.lorentzos.adp.util;

import com.lorentzos.adp.model.File;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by dionysis_lorentzos on 5/3/15
 * for package com.lorentzos.adp.util
 * Use with caution dinosaurs might appear!
 */
public class ComparatorFactory {

    private static enum ComparatorType{
        VERSION,
        DATE,
        COMMIT,
        BUILD_VARIANT
    }

    private static Comparator<File> builder(ComparatorType type) {
        switch (type){
            case COMMIT:
                return (lhs, rhs) -> stringComparator(lhs.getCommit(), rhs.getCommit());
            case VERSION:
                return (lhs, rhs) -> -stringComparator(lhs.getVersion(), rhs.getVersion());
            case BUILD_VARIANT:
                return (lhs, rhs) -> stringComparator(lhs.getBuildVariant(), rhs.getBuildVariant());
            case DATE:
                return (lhs, rhs) -> {
                    try {
                        Date date1 = DateTimeUtil.isoWithMicro.parse(lhs.getDate());
                        Date date2 = DateTimeUtil.isoWithMicro.parse(rhs.getDate());
                        return -date1.compareTo(date2);
                    } catch (ParseException e) {
                        return 0;
                    }
                };
            default:
                throw new IllegalArgumentException("Wrong comparator type passed in builder");
        }
    }

    private static int stringComparator(String ls, String rs){
        return ls.compareTo(rs);
    }


    /**
     * The list is based on the "sorting_by" array of the dialog
     *
     */
    public static Comparator<File> getComparatorType(int which) {
        ComparatorType type =null;
        switch(which){
            case 0:
                type = ComparatorType.DATE;
                break;
            case 1:
                type = ComparatorType.VERSION;
                break;
            case 2:
                type = ComparatorType.COMMIT;
                break;
            case 3:
                type = ComparatorType.BUILD_VARIANT;
        }
        return builder(type);
    }
}
