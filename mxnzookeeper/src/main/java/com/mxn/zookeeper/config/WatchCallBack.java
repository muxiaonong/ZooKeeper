package com.mxn.zookeeper.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * @program: mxnzookeeper
 * @ClassName WatchCallBack
 * @description:
 * @author: muxiaonong
 * @create: 2021-10-19 22:13
 * @Version 1.0
 **/
public class WatchCallBack implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {

    ZooKeeper zk ;
    MyConfig conf ;
    CountDownLatch cc = new CountDownLatch(1);

    public MyConfig getConf() {
        return conf;
    }

    public void setConf(MyConfig conf) {
        this.conf = conf;
    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }


    public void aWait(){
        //exists的异步实现版本
        zk.exists(ZKConstants.ZK_NODE,this,this ,"exists watch");
        try {
            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** @Author mxn
     * @Description //TODO 此回调用于检索节点的stat
     * @Date 21:24 2021/10/20
     * @param rc 调用返回的code或结果
     * @param path 传递给异步调用的路径
     * @param ctx 传递给异步调用的上下文对象
     * @param stat 指定路径上节点的Stat对象
     * @return 
     **/
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if(stat != null){
            //getData的异步实现版本
            zk.getData(ZKConstants.ZK_NODE,this,this,"status");
        }
    }


    /** @Author mxn
     * @Description //TODO  此回调用于检索节点的数据和stat
     * @Date 21:23 2021/10/20
     * @param rc 调用返回的code或结果
     * @param path 传递给异步调用的路径
     * @param ctx 传递给异步调用的上下文对象
     * @param data 节点的数据
     * @param stat 指定节点的Stat对象
     * @return
     **/
    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        if(data != null ){
            String s = new String(data);
            conf.setConf(s);
            cc.countDown();
        }
    }

    /** @Author mxn
     * @Description //TODO Watcher接口的实现。
     *                      Watcher接口指定事件处理程序类必须实现的公共接口。
     *                      ZooKeeper客户机将从它连接到的ZooKeeper服务器获取各种事件。
     *                      使用这种客户机的应用程序通过向客户机注册回调对象来处理这些事件。
     *                      回调对象应该是实现监视器接口的类的实例。
     * @Date 21:24 2021/10/20
     * @Param  watchedEvent WatchedEvent表示监视者能够响应的ZooKeeper上的更改。
     *          WatchedEvent包含发生了什么，
     *          ZooKeeper的当前状态，以及事件中涉及的znode的路径。
     * @return 
     **/
    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                //当一个node被创建后，获取node
                //getData中又会触发StatCallback的回调processResult
                zk.getData(ZKConstants.ZK_NODE,this,this,"sdfs");
                break;
            case NodeDeleted:
                //节点删除
                conf.setConf("");
                //重新开启CountDownLatch
                cc = new CountDownLatch(1);
                break;
            case NodeDataChanged:
                //节点数据被改变了
                //触发DataCallback的回调
                zk.getData(ZKConstants.ZK_NODE,this,this,"sdfs");
                break;
                //子节点发生变化的时候
            case NodeChildrenChanged:
                break;
        }


    }
}