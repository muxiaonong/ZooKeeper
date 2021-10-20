package com.mxn.zookeeper.config;

import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @program: mxnzookeeper
 * @ClassName ZKUtils
 * @description:
 * @author: muxiaonong
 * @create: 2021-10-19 21:59
 * @Version 1.0
 **/
public class ZKUtils {

    private static ZooKeeper zk;

    //192.168.5.130:2181/mxn 这个后面/mxn，表示客户端如果成功建立了到zk集群的连接，
    // 那么默认该客户端工作的根path就是/mxn，如果不带/mxn，默认根path是/
    //当然我们要保证/mxn这个节点在ZK上是存在的
    private static String address ="192.18.5.129:2181,192.168.5.130:2181,192.168.5.130:2181/mxn";

    private static DefaultWatch watch = new DefaultWatch();

    private static CountDownLatch init = new CountDownLatch(1);

    public static ZooKeeper getZK(){

        try {
            //因为是异步的，所以要await，等到连接上zk集群之后再进行后续操作
            zk = new ZooKeeper(address,1000,watch);
            watch.setCc(init);
            init.await();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return zk;
    }

}