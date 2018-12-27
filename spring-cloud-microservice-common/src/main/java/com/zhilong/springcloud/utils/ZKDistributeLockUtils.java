package com.zhilong.springcloud.utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;

public class ZKDistributeLockUtils {

    private static  final String SEPARATOR = "/";

    private static final String ZK_LOCK_PROJECT = "purchase-online";

    private static final String DISTRIBUTED_LOCK = "distributed-lock";

    private String nameSpace;

    private CuratorFramework client;

    private final Logger log = LoggerFactory.getLogger(ZKDistributeLockUtils.class);

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public ZKDistributeLockUtils(String nameSpace, CuratorFramework client) {
        this.nameSpace = nameSpace;
        this.client = client;
    }

    /**
     * Using namespace
     */
    private void init (){
        client.usingNamespace(nameSpace);
        /**
         *  create Zk namespace znode
         *    namespace : ZK-locks-nameSpace
         *                    |
         *                     --  purchase-online
         *                            |
         *                             -- distributed-lock
         */
        try{
            if(client.checkExists().forPath( SEPARATOR + ZK_LOCK_PROJECT) == null){

                //make sure you're connected to zookeeper.
                client.blockUntilConnected();

                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(SEPARATOR + ZK_LOCK_PROJECT);
            }
            addWatcherToLock(SEPARATOR + ZK_LOCK_PROJECT);
        }catch (Exception e){
            log.error("distribute lock initial failed");
        }
    }

    /**
     * Get DistributeLock. if got successfully, countDownLath -1 else wait all the time.
     * implements -- create child node DISTRIBUTED_LOCK
     */
    public void getLock(){

        /**
         * Using an infinite loop,
         * you can jump out if and only if the last lock was released and the current request obtained the lock
         */
        while (true){

            try{
                /**
                 * If the node can be created, the lock is not held
                 */
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        //.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(SEPARATOR + ZK_LOCK_PROJECT + SEPARATOR + DISTRIBUTED_LOCK);

                log.info("Got Distributed lock!");
                return;
            }catch (Exception e){
                log.error("Got Distributed lock failed!",e);
                try{
                    /**
                     * If a distributed lock is not obtained, the synchronous resource value needs to be reset
                     */
                    if(getCountDownLatch().getCount() <= 0){
                        log.info("*********************");
                        countDownLatch = new CountDownLatch(1);
                    }
                    countDownLatch.await();
                }catch (Exception e1){
                    log.error("set countDown failed！", e1);
                }
            }

        }
    }

    /**
     * DistributeLock release operation
     * @return
     */
    public boolean releaseLock(){

        try{
            if(client.checkExists().forPath(SEPARATOR + ZK_LOCK_PROJECT + SEPARATOR + DISTRIBUTED_LOCK) != null){
                client.delete().forPath(SEPARATOR + ZK_LOCK_PROJECT + SEPARATOR + DISTRIBUTED_LOCK);
            }
        }catch (Exception e){
            log.error("Failed to release Lock！", e);
            return false;
        }
        log.info("Released Lock！");
        return true;
    }

    /**
     * child node adds the watch event
     * and listens for the removal of the parent node node -- the release of the lock
     */
    public void addWatcherToLock(String path) throws Exception{
        final PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path, true);
        pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);

        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if(event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED){
                    String path = event.getData().getPath();
                    log.info("The last session ended or the lock path was released：{}" ,path);
                    if(path.contains(ZK_LOCK_PROJECT)){
                        log.info("Releases the counter so that the current thread can acquire a distributed lock");
                        countDownLatch.countDown();
                    }
                }
            }
        });
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
}
