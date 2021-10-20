package com.mxn.zookeeper.config;

import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;
import org.junit.Test;

/**
 * @program: mxnzookeeper
 * @ClassName TestConfig
 * @description:
 * @author: muxiaonong
 * @create: 2021-10-19 22:04
 * @Version 1.0
 **/
public class TestConfig {

    ZooKeeper zk;

    @Before
    public void conn(){
        zk = ZKUtils.getZK();
    }

    /** @Author mxn
     * @Description //TODO 关闭ZK
     * @Date 21:16 2021/10/20
     * @Param
     * @return
     **/
    public void close(){
        try {
            zk.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void getConf(){
        WatchCallBack watchCallBack = new WatchCallBack();
        watchCallBack.setZk(zk);
        MyConfig myConfig = new MyConfig();
        watchCallBack.setConf(myConfig);

        //阻塞等待
        watchCallBack.aWait();


        while(true){

            if(myConfig.getConf().equals("")){
                System.out.println("zk node 节点丢失了 ......");
                ////此时应该阻塞住，等待着node重新创建
                watchCallBack.aWait();
            }else{
                System.out.println(myConfig.getConf());

            }
//
            try {
                //每隔500毫秒打印一次
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

}