package upson.grant;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class CPUMemInfo
{
    static OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
    private Runtime runTime;

    public CPUMemInfo()
    {
        runTime = Runtime.getRuntime();
    }

    public String retrieveSystemInformation()
    {
        String result;
        result = retrieveMemoryUsage(runTime) + retrieveCPUUsage();
        return result;
    }

    public String retrieveMemoryUsage(Runtime runtime)
    {
        long total, free, used;
        int kb = 1024;

        total = runtime.totalMemory();
        free = runtime.freeMemory();
        used = total - free;

        return "Total Memory: " + total / kb + "KB \nMemory Used: " + used / kb + "KB \nMemory Free: " + free / kb + "KB \n" +
                "Percent Used: " + ((double)used/(double)total) * 100 + "% \n" + "Percent Free: " + ((double)free/(double)total) * 100 + "%\n";
    }

    public String retrieveCPUUsage()
    {
        int cpuCount = operatingSystemMXBean.getAvailableProcessors();
        long cpuStartTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();

        long elapsedStartTime = System.nanoTime();
        testSystem();
        long end = System.nanoTime();

        long totalAvailCPUTime = cpuCount * (end-elapsedStartTime);
        long totalUsedCPUTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()-cpuStartTime;
        float per = ((float)totalUsedCPUTime * 100)/(float)totalAvailCPUTime;
        return "CPU Usage: " + (int)per + "%";
    }

    public void testSystem()
    {
        for(long i = 0; i < 100; i++) {}
    }
}

