package org.pentaho.di.trans.steps.convertimages;

import java.io.IOException;
import java.io.File;

import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

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
            data.outputRowMeta = (RowMetaInterface) getInputRowMeta().clone();
            meta.getFields(data.outputRowMeta, getStepname(), null, null, this);
        }

        try {
            org.im4java.core.ConvertCmd convert = new org.im4java.core.ConvertCmd();
            convert.setSearchPath(meta.getProgramPath());
            org.im4java.core.IMOperation operation = new org.im4java.core.IMOperation();
            operation.addImage((String) r[data.outputRowMeta.indexOfValue(meta.getDynamicSourceFileNameField())]);
            operation.addImage((String) r[data.outputRowMeta.indexOfValue(meta.getDynamicTargetFileNameField())]);
            convert.run(operation);
        } catch (IOException e) {
            throw new KettleException(e);
        } catch (InterruptedException e) {
            throw new KettleException(e);
        } catch (org.im4java.core.IM4JavaException e) {
            throw new KettleException(e);
        }

//        try {
//            String targetFileName = (String) r[data.outputRowMeta.indexOfValue(meta.getDynamicTargetFileNameField())];
//            ImageInfo source = new ImageInfo((String) r[data.outputRowMeta.indexOfValue(meta.getDynamicSourceFileNameField())]);
//            ImageInfo target = new ImageInfo(targetFileName);
//            MagickImage converter = new MagickImage(source);
//            converter.setFileName(targetFileName);
//            converter.writeImage(target);
//        } catch (MagickException e) {
//            throw new KettleException(e);
//        }
        
        putRow(data.outputRowMeta, r);

        return true;
    }

    public void dipose(StepMetaInterface smi, StepDataInterface sdi) {
        ConvertImagesMeta meta = (ConvertImagesMeta) smi;
        ConvertImagesData data = (ConvertImagesData) sdi;

        super.dispose(meta, data);
    }
}
