package org.femtoframework.util;


import java.util.ArrayList;
import java.util.List;

/**
 * The class is for debug
 *
 * @author fengyun
 * @version 2.0
 */
public class TimeWorker implements java.io.Serializable
{
    public static final int INITIAL_SIZE = 5;

    private List<String> description = null;
    private List<Long> points = null;

    public TimeWorker()
    {
        this(INITIAL_SIZE);
    }

    public TimeWorker(int initialSize)
    {
        description = new ArrayList<>(initialSize);
        points = new ArrayList<>(initialSize);
    }

    public void addTimePoint()
    {
        points.add(System.currentTimeMillis());
        description.add(String.valueOf(points.size()));
    }

    public void addTimePoint(String desc)
    {
        points.add(System.currentTimeMillis());
        description.add(desc);
    }

    public int getSize()
    {
        return points.size();
    }

    public long getTimeBetween(int startindex, int endindex)
    {
        if (startindex < 0 || startindex > endindex
                || endindex > points.size()) {
            return 0L;
        }
        Long start = points.get(startindex);
        Long end = points.get(endindex);
        return end - start;
    }

    public long[] getTimes()
    {
        if (points.size() <= 1) {
            return new long[0];
        }
        Long prev = points.get(0);
        long[] times = new long[points.size() - 1];
        for (int i = 1; i < points.size(); i++) {
            Long next = points.get(i);
            times[i - 1] = next - prev;
            prev = next;
        }
        return times;
    }

    public void clearTimePoints()
    {
        points.clear();
        description.clear();
    }

    public void printResult()
    {
        long[] times = getTimes();
        System.out.println("---------------------------------------------------");
        System.out.println(description.get(0));
        System.out.println("---------------------------------------------------");
        for (int i = 0; i < times.length; i++) {
            System.out.println(description.get(i + 1) + " :" + times[i]);
        }
        System.out.println("---------------------------------------------------");
    }
}
