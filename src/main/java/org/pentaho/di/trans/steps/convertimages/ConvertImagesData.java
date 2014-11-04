package org.pentaho.di.trans.steps.convertimages;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

public class ConvertImagesData extends BaseStepData implements StepDataInterface {
    public RowMetaInterface outputRowMeta;

    public ConvertImagesData() {
        super();
    }
}
