package com.mxn.zookeeper.config;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * @program: mxnzookeeper
 * @ClassName DefaultWatch
 * @description:
 * @author: muxiaonong
 * @create: 2021-10-19 22:02
 * @Version 1.0
 **/
public class DefaultWatch implements Watcher {

    CountDownLatch cc;

    public void setCc(CountDownLatch cc) {
        this.cc = cc;
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println(event.toString());

        switch (event.getState()) {
            case Unknown:
                break;
            case Disconnected:
                break;
            case NoSyncConnected:
                break;
            case SyncConnected:
                System.out.println("连接成功。。。。。");
                //连接成功后，执行countDown，此时便可以拿zk对象使用了
                cc.countDown();
                break;
            case AuthFailed:
                break;
            case ConnectedReadOnly:
                break;
            case SaslAuthenticated:
                break;
            case Expired:
                break;
            case Closed:
                break;
        }

    }
}