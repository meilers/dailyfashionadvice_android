package com.sobremesa.waywt.synchronizers.preprocessors;


import com.sobremesa.waywt.models.RemoteObject;

import java.util.List;

/**
 * Created by Michael on 2014-03-11.
 */
public abstract class BasePreProcessor<T extends RemoteObject> {

    public abstract void preProcessRemoteRecords(List<T> records);

}