package org.pentaho.di.trans.steps.convertimages;

import java.io.File;
import java.io.IOException;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.IM4JavaException;

public class ConvertImages extends BaseStep implements StepInterface {
    private static Class<?> PKG = ConvertImagesMeta.class;

    public ConvertImages(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
        super(s, stepDataInterface, c, t, dis);
    }

    public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
        ConvertImagesMeta meta = (ConvertImagesMeta) smi;
        ConvertImagesData data = (ConvertImagesData) sdi;

        System.setProperty("jmagick.systemclassloader", "no");
        return super.init(meta, data);
    }

    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
        ConvertImagesMeta meta = (ConvertImagesMeta) smi;
        ConvertImagesData data = (ConvertImagesData) sdi;

        Object[] r = getRow();

        if (r == null) {
            setOutputDone();
            return false;
        }

        if (first) {
            first = false;
            data.outputRowMeta = getInputRowMeta().clone();
            meta.getFields(data.outputRowMeta, getStepname(), null, null, this);
        }

        try {
            String source = (String) r[data.outputRowMeta.indexOfValue(meta.getDynamicSourceFileNameField())];
            String target = (String) r[data.outputRowMeta.indexOfValue(meta.getDynamicTargetFileNameField())];
            File sourceFile = new File(source);
            File targetFile = new File(target);
            File targetFolder = targetFile.getParentFile();

            if (!targetFile.exists() || meta.isOverwriteTarget()) {
                if (!targetFolder.exists() && meta.isCreateParentFolder()) {
                    targetFolder.mkdir();
                }

                if (targetFolder.exists()) {
                    ConvertCmd convert = new ConvertCmd();
                    convert.setSearchPath(meta.getProgramPath());
                    IMOperation operation = new IMOperation();
                    operation.addImage((String) r[data.outputRowMeta.indexOfValue(meta.getDynamicSourceFileNameField())]);
                    operation.addImage((String) r[data.outputRowMeta.indexOfValue(meta.getDynamicTargetFileNameField())]);
                    convert.run(operation);
                }
            }

            if (meta.isDeleteSource()) {
                sourceFile.delete();
            }
        } catch (IOException e) {
            throw new KettleException(e);
        } catch (InterruptedException e) {
            throw new KettleException(e);
        } catch (IM4JavaException e) {
            throw new KettleException(e);
        }

        putRow(data.outputRowMeta, r);

        return true;
    }

    public void dipose(StepMetaInterface smi, StepDataInterface sdi) {
        ConvertImagesMeta meta = (ConvertImagesMeta) smi;
        ConvertImagesData data = (ConvertImagesData) sdi;

        super.dispose(meta, data);
    }
}
