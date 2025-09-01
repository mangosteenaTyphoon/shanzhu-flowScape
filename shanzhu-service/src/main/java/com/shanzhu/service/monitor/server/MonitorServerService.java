package com.shanzhu.service.monitor.server;

import com.shanzhu.model.monitor.ServerInfo;

public interface MonitorServerService {

    /**
     * 获取服务器运行数据
     */
    ServerInfo serverInfo();
}
