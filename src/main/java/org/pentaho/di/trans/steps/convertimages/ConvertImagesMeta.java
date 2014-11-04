package org.pentaho.di.trans.steps.convertimages;

import java.util.List;

import org.eclipse.swt.widgets.Shell;

import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.ui.trans.steps.convertimages.ConvertImagesDialog;
import org.pentaho.metastore.api.IMetaStore;

import org.w3c.dom.Node;

public class ConvertImagesMeta extends BaseStepMeta implements StepMetaInterface {
    private static Class<?> PKG = ConvertImagesMeta.class;
    private String programPath;
    private boolean deleteSource;
    private boolean createParentFolder;
    private boolean overwriteTarget;
    private String sourceFileNameField;
    private String targetFileNameField;

    public ConvertImagesMeta() {
        super();
    }

    public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {
        return new ConvertImagesDialog(shell, meta, transMeta, name);
    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {
        return new ConvertImages(stepMeta, stepDataInterface, cnr, transMeta, disp);
    }

    public StepDataInterface getStepData() {
        return new ConvertImagesData();
    }

    public void setDefault() {
        programPath = "";
        deleteSource = false;
        createParentFolder = false;
        overwriteTarget = false;
    }

    public String getProgramPath() {
        return programPath;
    }

    public void setProgramPath(String programPath) {
        this.programPath = programPath;
    }

    public String getDynamicSourceFileNameField() {
        return sourceFileNameField;
    }

    public void setDynamicSourceFileNameField(String sourceFileNameField) {
        this.sourceFileNameField = sourceFileNameField;
    }

    public String getDynamicTargetFileNameField() {
        return targetFileNameField;
    }

    public void setDynamicTargetFileNameField(String targetFileNameField) {
        this.targetFileNameField = targetFileNameField;
    }

    public boolean isDeleteSource() {
        return deleteSource;
    }

    public void setDeleteSource(boolean deleteSource) {
        this.deleteSource = deleteSource;
    }

    public boolean isOverwriteTarget() {
        return overwriteTarget;
    }

    public void setOverwriteTarget(boolean overwriteTarget) {
        this.overwriteTarget = overwriteTarget;
    }

    public boolean isCreateParentFolder() {
        return createParentFolder;
    }

    public void setCreateParentFolder(boolean createParentFolder) {
        this.createParentFolder = createParentFolder;
    }

    public Object clone() {
        return (ConvertImagesMeta) super.clone();
    }

    public void getFields(RowMetaInterface r, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) {
    }

    public String getXML() {
        StringBuffer xml = new StringBuffer();

        xml.append("    " + XMLHandler.addTagValue("programPath", programPath));
        xml.append("    " + XMLHandler.addTagValue("sourceFileNameField", sourceFileNameField));
        xml.append("    " + XMLHandler.addTagValue("targetFileNameField", targetFileNameField));
        xml.append("    " + XMLHandler.addTagValue("deleteSource", deleteSource));
        xml.append("    " + XMLHandler.addTagValue("createParentFolder", createParentFolder));
        xml.append("    " + XMLHandler.addTagValue("overwriteTarget", overwriteTarget));

        return xml.toString();
    }

    public void loadXML(Node stepNode, List<DatabaseMeta> databases, IMetaStore metaStore) throws KettleXMLException {
        try {
            programPath = XMLHandler.getTagValue(stepNode, "programPath");
            sourceFileNameField = XMLHandler.getTagValue(stepNode, "sourceFileNameField");
            targetFileNameField = XMLHandler.getTagValue(stepNode, "targetFileNameField");
            deleteSource = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepNode, "deleteSource"));
            createParentFolder = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepNode, "createParentFolder"));
            overwriteTarget = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepNode, "overwriteTarget"));
        } catch (Exception e) {
            throw new KettleXMLException(BaseMessages.getString(PKG, "ConvertImagesMeta.Exception.UnableToReadStepInfo"), e);
        }
    }

    public void readRep(Repository repo, IMetaStore metaStore, ObjectId stepId, List<DatabaseMeta> databases) throws KettleException {
        try {
            programPath = repo.getStepAttributeString(stepId, "programPath");
            sourceFileNameField = repo.getStepAttributeString(stepId, "sourceFileNameField");
            targetFileNameField = repo.getStepAttributeString(stepId, "targetFileNameField");
            deleteSource = repo.getStepAttributeBoolean(stepId, "deleteSource");
            createParentFolder = repo.getStepAttributeBoolean(stepId, "createParentFolder");
            overwriteTarget = repo.getStepAttributeBoolean(stepId, "overwriteTarget");
        } catch (Exception e) {
            throw new KettleException(BaseMessages.getString(PKG, "ConvertImagesMeta.Exception.UnexpectedErrorReadingStepInfo"), e);
        }
    }

    public void saveRep(Repository rep, IMetaStore metaStore, ObjectId transformationId, ObjectId stepId) throws KettleException {
        try {
            rep.saveStepAttribute(transformationId, stepId, "programPath", programPath);
            rep.saveStepAttribute(transformationId, stepId, "sourceFileNameField", sourceFileNameField);
            rep.saveStepAttribute(transformationId, stepId, "targetFileNameField", targetFileNameField);
            rep.saveStepAttribute(transformationId, stepId, "deleteSource", deleteSource);
            rep.saveStepAttribute(transformationId, stepId, "createParentFolder", createParentFolder);
            rep.saveStepAttribute(transformationId, stepId, "overwriteTarget", overwriteTarget);
        } catch (Exception e) {
            throw new KettleException(BaseMessages.getString(PKG, "ConvertImagesMeta.Exception.UnableToSaveStepInfo") + stepId, e);
        }
    }
}
