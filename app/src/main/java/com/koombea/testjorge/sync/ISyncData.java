package com.koombea.testjorge.sync;

import com.koombea.testjorge.common.ActivityBase;

public interface ISyncData {
    void download(ActivityBase context) throws Exception;
}
