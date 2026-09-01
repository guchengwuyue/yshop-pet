package co.yixiang.yshop.module.crm.scheduler;


import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.*;

/**
 * 基于 ScheduledExecutorService 的一次性任务调度器
 * 
 * 优势：
 * 1. 支持线程池，可以并发执行多个任务
 * 2. 任务异常不会影响其他任务
 * 3. 更好的性能和资源管理
 * 4. 支持任务取消和状态查询
 *
 * @author yshop
 */
@Component
@Slf4j
public class OnceJobScheduler {

    private final ScheduledExecutorService scheduler;
    
    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public OnceJobScheduler() {
        this(
            Executors.newScheduledThreadPool(
                Math.max(4, Runtime.getRuntime().availableProcessors()),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("once-job-scheduler-" + thread.getId());
                    thread.setDaemon(true);
                    return thread;
                }
            )
        );
    }

    public OnceJobScheduler(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * 在指定时间执行一次任务
     *
     * @param taskName 任务名称（唯一标识）
     * @param task 任务逻辑
     * @param executeTime 执行时间
     * @return 任务ID
     */
    public String scheduleAt(String taskName, Runnable task, LocalDateTime executeTime) {
        if (executeTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("执行时间不能是过去的时间: " + executeTime);
        }

        Date executeDate = java.sql.Timestamp.valueOf(executeTime);
        return scheduleAt(taskName, task, executeDate);
    }

    /**
     * 在指定时间执行一次任务
     *
     * @param taskName 任务名称（唯一标识）
     * @param task 任务逻辑
     * @param executeTime 执行时间
     * @return 任务ID
     */
    public String scheduleAt(String taskName, Runnable task, Date executeTime) {
        if (executeTime.before(new Date())) {
            throw new IllegalArgumentException("执行时间不能是过去的时间: " + executeTime);
        }

        if (scheduledTasks.containsKey(taskName)) {
            log.warn("任务已存在，先取消旧任务: {}", taskName);
            cancel(taskName);
        }

        long delay = executeTime.getTime() - System.currentTimeMillis();
        
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            try {
                log.info("开始执行一次性任务: {}", taskName);
                task.run();
                log.info("一次性任务执行成功: {}", taskName);
            } catch (Exception e) {
                log.error("一次性任务执行失败: {}", taskName, e);
            } finally {
                scheduledTasks.remove(taskName);
            }
        }, delay, TimeUnit.MILLISECONDS);

        scheduledTasks.put(taskName, future);
        log.info("任务已调度: {}, 执行时间: {}, 延迟: {}ms", taskName, executeTime, delay);
        
        return taskName;
    }

    /**
     * 延迟执行一次任务
     *
     * @param taskName 任务名称（唯一标识）
     * @param task 任务逻辑
     * @param delay 延迟时间
     * @param unit 时间单位
     * @return 任务ID
     */
    public String scheduleAfterDelay(String taskName, Runnable task, long delay, TimeUnit unit) {
        if (delay <= 0) {
            throw new IllegalArgumentException("延迟时间必须大于0");
        }

        if (scheduledTasks.containsKey(taskName)) {
            log.warn("任务已存在，先取消旧任务: {}", taskName);
            cancel(taskName);
        }

        ScheduledFuture<?> future = scheduler.schedule(() -> {
            try {
                log.info("开始执行延迟任务: {}", taskName);
                task.run();
                log.info("延迟任务执行成功: {}", taskName);
            } catch (Exception e) {
                log.error("延迟任务执行失败: {}", taskName, e);
            } finally {
                scheduledTasks.remove(taskName);
            }
        }, delay, unit);

        scheduledTasks.put(taskName, future);
        log.info("延迟任务已调度: {}, 延迟: {} {}", taskName, delay, unit);
        
        return taskName;
    }

    /**
     * 延迟执行一次任务（秒）
     *
     * @param taskName 任务名称（唯一标识）
     * @param task 任务逻辑
     * @param delaySeconds 延迟秒数
     * @return 任务ID
     */
    public String scheduleAfterDelay(String taskName, Runnable task, long delaySeconds) {
        return scheduleAfterDelay(taskName, task, delaySeconds, TimeUnit.SECONDS);
    }

    /**
     * 延迟执行一次任务（毫秒）
     *
     * @param taskName 任务名称（唯一标识）
     * @param task 任务逻辑
     * @param delayMillis 延迟毫秒数
     * @return 任务ID
     */
    public String scheduleAfterDelayMillis(String taskName, Runnable task, long delayMillis) {
        return scheduleAfterDelay(taskName, task, delayMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * 立即执行任务（异步）
     *
     * @param taskName 任务名称
     * @param task 任务逻辑
     * @return 任务ID
     */
    public String scheduleNow(String taskName, Runnable task) {
        return scheduleAfterDelayMillis(taskName, task, 0);
    }

    /**
     * 提交任务并返回 Future，可以获取执行结果
     *
     * @param taskName 任务名称
     * @param task 任务逻辑（带返回值）
     * @param executeTime 执行时间
     * @return ScheduledFuture
     */
    public <V> ScheduledFuture<V> scheduleWithResult(String taskName, Callable<V> task, LocalDateTime executeTime) {
        if (executeTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("执行时间不能是过去的时间: " + executeTime);
        }

        long delay = java.sql.Timestamp.valueOf(executeTime).getTime() - System.currentTimeMillis();
        
        ScheduledFuture<V> future = scheduler.schedule(() -> {
            try {
                log.info("开始执行带返回值的一次性任务: {}", taskName);
                V result = task.call();
                log.info("带返回值任务执行成功: {}, 结果: {}", taskName, result);
                return result;
            } catch (Exception e) {
                log.error("带返回值任务执行失败: {}", taskName, e);
                throw new CompletionException(e);
            } finally {
                scheduledTasks.remove(taskName);
            }
        }, delay, TimeUnit.MILLISECONDS);

        scheduledTasks.put(taskName, future);
        return future;
    }

    /**
     * 取消任务
     *
     * @param taskName 任务名称
     * @return 是否取消成功
     */
    public boolean cancel(String taskName) {
        ScheduledFuture<?> future = scheduledTasks.remove(taskName);
        if (future != null) {
            boolean cancelled = future.cancel(false);
            log.info("任务{}: {}", cancelled ? "已取消" : "已完成或无法取消", taskName);
            return cancelled;
        }
        log.warn("任务不存在或已完成: {}", taskName);
        return false;
    }

    /**
     * 强制取消任务（中断正在执行的任务）
     *
     * @param taskName 任务名称
     * @return 是否取消成功
     */
    public boolean cancelNow(String taskName) {
        ScheduledFuture<?> future = scheduledTasks.remove(taskName);
        if (future != null) {
            boolean cancelled = future.cancel(true);
            log.info("任务{}（强制）: {}", cancelled ? "已取消" : "已完成或无法取消", taskName);
            return cancelled;
        }
        log.warn("任务不存在或已完成: {}", taskName);
        return false;
    }

    /**
     * 检查任务是否存在
     *
     * @param taskName 任务名称
     * @return 是否存在
     */
    public boolean containsTask(String taskName) {
        return scheduledTasks.containsKey(taskName);
    }

    /**
     * 检查任务是否完成
     *
     * @param taskName 任务名称
     * @return 是否完成
     */
    public boolean isDone(String taskName) {
        ScheduledFuture<?> future = scheduledTasks.get(taskName);
        return future != null && future.isDone();
    }

    /**
     * 检查任务是否已取消
     *
     * @param taskName 任务名称
     * @return 是否已取消
     */
    public boolean isCancelled(String taskName) {
        ScheduledFuture<?> future = scheduledTasks.get(taskName);
        return future != null && future.isCancelled();
    }

    /**
     * 获取待执行任务数量
     *
     * @return 待执行任务数量
     */
    public int getScheduledTaskCount() {
        return scheduledTasks.size();
    }

    /**
     * 获取所有待执行任务名称
     *
     * @return 任务名称集合
     */
    public java.util.Set<String> getScheduledTaskNames() {
        return scheduledTasks.keySet();
    }

    /**
     * 等待所有任务完成
     *
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 是否全部完成
     * @throws InterruptedException 中断异常
     */
    public boolean waitForAllTasks(long timeout, TimeUnit unit) throws InterruptedException {
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        
        while (!scheduledTasks.isEmpty()) {
            long remaining = deadline - System.nanoTime();
            if (remaining <= 0) {
                log.warn("等待任务完成超时，剩余任务数: {}", scheduledTasks.size());
                return false;
            }
            
            Thread.sleep(Math.min(100, unit.toMillis(remaining)));
        }
        
        return true;
    }

    /**
     * 销毁时清理所有任务
     */
    @PreDestroy
    public void destroy() {
        log.info("正在清理所有一次性任务，当前任务数: {}", scheduledTasks.size());
        
        scheduledTasks.forEach((name, future) -> {
            future.cancel(false);
            log.info("已取消任务: {}", name);
        });
        scheduledTasks.clear();
        
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                log.warn("调度器未能在5秒内关闭，强制关闭");
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("关闭调度器时被中断", e);
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        log.info("所有一次性任务已清理");
    }

}
