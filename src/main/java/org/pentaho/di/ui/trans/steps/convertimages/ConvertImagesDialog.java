package org.pentaho.di.ui.trans.steps.convertimages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.steps.convertimages.ConvertImagesMeta;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class ConvertImagesDialog extends BaseStepDialog implements StepDialogInterface {
    private static Class<?> PKG = ConvertImagesMeta.class;
    private boolean gotPreviousFields;
    private ConvertImagesMeta meta;

    private Label wlProgramPath;
    private Text wProgramPath;
    private FormData fdlProgramPath, fdProgramPath;

    private Group wSettingsGroup;
    private FormData fdSettingsGroup;

    private Label wlDeleteSource;
    private Button wDeleteSource;
    private FormData fdlDeleteSource, fdDeleteSource;

    private Label wlCreateParentFolder;
    private Button wCreateParentFolder;
    private FormData fdlCreateParentFolder, fdCreateParentFolder;

    private Label wlOverwriteTarget;
    private Button wOverwriteTarget;
    private FormData fdlOverwriteTarget, fdOverwriteTarget;

    private Label wlSourceFileNameField;
    private CCombo wSourceFileNameField;
    private FormData fdlSourceFileNameField, fdSourceFileNameField;

    private Label wlTargetFileNameField;
    private CCombo wTargetFileNameField;
    private FormData fdlTargetFileNameField, fdTargetFileNameField;

    private Label wlErrorMessageField;
    private Text wErrorMessageField;
    private FormData fdlErrorMessageField, fdErrorMessageField;

    public ConvertImagesDialog(Shell parent, Object in, TransMeta transMeta, String sname) {
        super(parent, (BaseStepMeta) in, transMeta, sname);
        meta = (ConvertImagesMeta) in;
    }

    public String open() {
        Shell parent = getParent();
        Display display = parent.getDisplay();

        shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
        props.setLook(shell);
        setShellImage(shell, meta);

        changed = meta.hasChanged();

        ModifyListener lsMod = new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                meta.setChanged();
            }
        };

        SelectionAdapter lsButtonChanged = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                meta.setChanged();
            }
        };

        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;

        shell.setLayout(formLayout);
        shell.setText(BaseMessages.getString(PKG, "ConvertImagesDialog.Shell.Title"));

        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;

        wlStepname = new Label(shell, SWT.RIGHT);
        wlStepname.setText(BaseMessages.getString(PKG, "System.Label.StepName"));
        props.setLook(wlStepname);
        fdlStepname = new FormData();
        fdlStepname.left = new FormAttachment(0, 0);
        fdlStepname.right = new FormAttachment(middle, -margin);
        fdlStepname.top = new FormAttachment(0, margin);
        wlStepname.setLayoutData(fdlStepname);

        wStepname = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        wStepname.setText(stepname);
        props.setLook(wStepname);
        wStepname.addModifyListener(lsMod);
        fdStepname = new FormData();
        fdStepname.left = new FormAttachment(middle, 0);
        fdStepname.right = new FormAttachment(100, 0);
        fdStepname.top = new FormAttachment(0, margin);
        wStepname.setLayoutData(fdStepname);

        wlProgramPath = new Label(shell, SWT.RIGHT);
        wlProgramPath.setText(BaseMessages.getString(PKG, "ConvertImagesDialog.ProgramPath.Label"));
        props.setLook(wlProgramPath);
        fdlProgramPath = new FormData();
        fdlProgramPath.left = new FormAttachment(0, 0);
        fdlProgramPath.top = new FormAttachment(wStepname, margin);
        fdlProgramPath.right = new FormAttachment(middle, -margin);
        wlProgramPath.setLayoutData(fdlProgramPath);

        wProgramPath = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        props.setLook(wProgramPath);
        wProgramPath.addModifyListener(lsMod);
        fdProgramPath = new FormData();
        fdProgramPath.left = new FormAttachment(middle, 0);
        fdProgramPath.right = new FormAttachment(100, -margin);
        fdProgramPath.top = new FormAttachment(wStepname, 2 * margin);
        wProgramPath.setLayoutData(fdProgramPath);

        wSettingsGroup = new Group(shell, SWT.SHADOW_NONE);
        props.setLook(wSettingsGroup);
        wSettingsGroup.setText(BaseMessages.getString(PKG, "ConvertImagesDialog.wSettingsGroup.Label"));

        FormLayout settingsGroupLayout = new FormLayout();
        settingsGroupLayout.marginWidth = 10;
        settingsGroupLayout.marginHeight = 10;
        wSettingsGroup.setLayout(settingsGroupLayout);

        wlDeleteSource = new Label(wSettingsGroup, SWT.RIGHT);
        wlDeleteSource.setText(BaseMessages.getString(PKG, "ConvertImagesDialog.DeleteSource.Label"));
        props.setLook(wlDeleteSource);
        fdlDeleteSource = new FormData();
        fdlDeleteSource.left = new FormAttachment(0, 0);
        fdlDeleteSource.top = new FormAttachment(wSettingsGroup, margin);
        fdlDeleteSource.right = new FormAttachment(middle, -margin);
        wlDeleteSource.setLayoutData(fdlDeleteSource);

        wDeleteSource = new Button(wSettingsGroup, SWT.CHECK);
        props.setLook(wDeleteSource);
        wDeleteSource.setToolTipText(BaseMessages.getString(PKG, "ConvertImagesDialog.DeleteSource.Tooltip"));
        wDeleteSource.addSelectionListener(lsButtonChanged);
        fdDeleteSource = new FormData();
        fdDeleteSource.left = new FormAttachment(middle, 0);
        fdDeleteSource.top = new FormAttachment(wSettingsGroup, margin);
        wDeleteSource.setLayoutData(fdDeleteSource);

        wlCreateParentFolder = new Label(wSettingsGroup, SWT.RIGHT);
        wlCreateParentFolder.setText(BaseMessages.getString(PKG, "ConvertImagesDialog.CreateParentFolder.Label"));
        props.setLook(wlCreateParentFolder);
        fdlCreateParentFolder = new FormData();
        fdlCreateParentFolder.left = new FormAttachment(0, 0);
        fdlCreateParentFolder.top = new FormAttachment(wDeleteSource, margin);
        fdlCreateParentFolder.right = new FormAttachment(middle, -margin);
        wlCreateParentFolder.setLayoutData(fdlCreateParentFolder);

        wCreateParentFolder = new Button(wSettingsGroup, SWT.CHECK);
        props.setLook(wCreateParentFolder);
        wCreateParentFolder.setToolTipText(BaseMessages.getString(PKG, "ConvertImagesDialog.CreateParentFolder.Tooltip"));
        wCreateParentFolder.addSelectionListener(lsButtonChanged);
        fdCreateParentFolder = new FormData();
        fdCreateParentFolder.left = new FormAttachment(middle, 0);
        fdCreateParentFolder.top = new FormAttachment(wDeleteSource, margin);
        wCreateParentFolder.setLayoutData(fdCreateParentFolder);

        wlOverwriteTarget = new Label(wSettingsGroup, SWT.RIGHT);
        wlOverwriteTarget.setText(BaseMessages.getString(PKG, "ConvertImagesDialog.OverwriteTarget.Label"));
        props.setLook(wlOverwriteTarget);
        fdlOverwriteTarget = new FormData();
        fdlOverwriteTarget.left = new FormAttachment(0, 0);
        fdlOverwriteTarget.top = new FormAttachment(wCreateParentFolder, margin);
        fdlOverwriteTarget.right = new FormAttachment(middle, -margin);
        wlOverwriteTarget.setLayoutData(fdlOverwriteTarget);

        wOverwriteTarget = new Button(wSettingsGroup, SWT.CHECK);
        props.setLook(wOverwriteTarget);
        wOverwriteTarget.setToolTipText(BaseMessages.getString(PKG, "ConvertImagesDialog.OverwriteTarget.Tooltip"));
        wOverwriteTarget.addSelectionListener(lsButtonChanged);
        fdOverwriteTarget = new FormData();
        fdOverwriteTarget.left = new FormAttachment(middle, 0);
        fdOverwriteTarget.top = new FormAttachment(wCreateParentFolder, margin);
        wOverwriteTarget.setLayoutData(fdOverwriteTarget);

        fdSettingsGroup = new FormData();
        fdSettingsGroup.left = new FormAttachment(0, margin);
        fdSettingsGroup.top = new FormAttachment(wProgramPath, margin);
        fdSettingsGroup.right = new FormAttachment(100, -margin);
        wSettingsGroup.setLayoutData(fdSettingsGroup);

        wlSourceFileNameField = new Label(shell, SWT.RIGHT);
        wlSourceFileNameField.setText(BaseMessages.getString(PKG, "ConvertImagesDialog.SourceFileNameField.Label"));
        props.setLook(wlSourceFileNameField);
        fdlSourceFileNameField = new FormData();
        fdlSourceFileNameField.left = new FormAttachment(0, 0);
        fdlSourceFileNameField.right = new FormAttachment(middle, -margin);
        fdlSourceFileNameField.top = new FormAttachment(wSettingsGroup, margin);
        wlSourceFileNameField.setLayoutData(fdlSourceFileNameField);

        wSourceFileNameField = new CCombo(shell, SWT.BORDER | SWT.READ_ONLY);
        props.setLook(wSourceFileNameField);
        wSourceFileNameField.setEditable(true);
        wSourceFileNameField.addModifyListener(lsMod);
        fdSourceFileNameField = new FormData();
        fdSourceFileNameField.left = new FormAttachment(middle, 0);
        fdSourceFileNameField.top = new FormAttachment(wSettingsGroup, 2 * margin);
        fdSourceFileNameField.right = new FormAttachment(100, -margin);
        wSourceFileNameField.setLayoutData(fdSourceFileNameField);
        wSourceFileNameField.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
                Cursor busy = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
                shell.setCursor(busy);
                get();
                shell.setCursor(null);
                busy.dispose();
            }
        });

        wlTargetFileNameField = new Label(shell, SWT.RIGHT);
        wlTargetFileNameField.setText(BaseMessages.getString(PKG, "ConvertImagesDialog.TargetFileNameField.Label"));
        props.setLook(wlTargetFileNameField);
        fdlTargetFileNameField = new FormData();
        fdlTargetFileNameField.left = new FormAttachment(0, 0);
        fdlTargetFileNameField.right = new FormAttachment(middle, -margin);
        fdlTargetFileNameField.top = new FormAttachment(wSourceFileNameField, margin);
        wlTargetFileNameField.setLayoutData(fdlTargetFileNameField);

        wTargetFileNameField = new CCombo(shell, SWT.BORDER | SWT.READ_ONLY);
        props.setLook(wTargetFileNameField);
        wTargetFileNameField.setEditable(true);
        wTargetFileNameField.addModifyListener(lsMod);
        fdTargetFileNameField = new FormData();
        fdTargetFileNameField.left = new FormAttachment(middle, 0);
        fdTargetFileNameField.top = new FormAttachment(wSourceFileNameField, 2 * margin);
        fdTargetFileNameField.right = new FormAttachment(100, -margin);
        wTargetFileNameField.setLayoutData(fdTargetFileNameField);
        wTargetFileNameField.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
                Cursor busy = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
                shell.setCursor(busy);
                get();
                shell.setCursor(null);
                busy.dispose();
            }
        });

        wlErrorMessageField = new Label(shell, SWT.RIGHT);
        wlErrorMessageField.setText(BaseMessages.getString(PKG, "ConvertImagesDialog.ErrorMessageField.Label"));
        props.setLook(wlErrorMessageField);
        fdlErrorMessageField = new FormData();
        fdlErrorMessageField.left = new FormAttachment(0, 0);
        fdlErrorMessageField.right = new FormAttachment(middle, -margin);
        fdlErrorMessageField.top = new FormAttachment(wTargetFileNameField, margin);
        wlErrorMessageField.setLayoutData(fdlErrorMessageField);

        wErrorMessageField = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        props.setLook(wErrorMessageField);
        wErrorMessageField.setEditable(true);
        wErrorMessageField.addModifyListener(lsMod);
        fdErrorMessageField = new FormData();
        fdErrorMessageField.left = new FormAttachment(middle, 0);
        fdErrorMessageField.top = new FormAttachment(wTargetFileNameField, 2 * margin);
        fdErrorMessageField.right = new FormAttachment(100, -margin);
        wErrorMessageField.setLayoutData(fdErrorMessageField);

        wOK = new Button(shell, SWT.PUSH);
        wOK.setText(BaseMessages.getString(PKG, "System.Button.OK"));
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"));

        BaseStepDialog.positionBottomButtons(shell, new Button[] { wOK, wCancel }, margin, wErrorMessageField);

        lsCancel = new Listener() {
            public void handleEvent(Event e) { cancel(); }
        };

        lsOK = new Listener() {
            public void handleEvent(Event e) { ok(); }
        };

        wCancel.addListener(SWT.Selection, lsCancel);
        wOK.addListener(SWT.Selection, lsOK);

        lsDef = new SelectionAdapter() {
            public void widgetDefaultSelected(SelectionEvent e) { ok(); }
        };

        wStepname.addSelectionListener(lsDef);

        shell.addShellListener(new ShellAdapter() {
            public void shellClosed(ShellEvent e) { cancel(); }
        });

        setSize();
        populateDialog();
        meta.setChanged(changed);

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }

        return stepname;
    }

    private void populateDialog() {
        if (log.isDebug()) {
            logDebug(BaseMessages.getString(PKG, "ConvertImagesDialog.Log.GettingKeyInfo"));
        }

        if (meta.getProgramPath() != null) {
            wProgramPath.setText(meta.getProgramPath());
        }

        if (meta.getDynamicSourceFileNameField() != null) {
            wSourceFileNameField.setText(meta.getDynamicSourceFileNameField());
        }

        if (meta.getDynamicTargetFileNameField() != null) {
            wTargetFileNameField.setText(meta.getDynamicTargetFileNameField());
        }

        if (meta.getErrorMessageField() != null) {
            wErrorMessageField.setText(meta.getErrorMessageField());
        }

        wDeleteSource.setSelection(meta.isDeleteSource());
        wCreateParentFolder.setSelection(meta.isCreateParentFolder());
        wOverwriteTarget.setSelection(meta.isOverwriteTarget());
        wStepname.selectAll();
        wStepname.setFocus();
    }

    private void cancel() {
        stepname = null;
        meta.setChanged(changed);
        dispose();
    }

    private void ok() {
        if (Const.isEmpty(wStepname.getText())) {
            return;
        }

        meta.setProgramPath(wProgramPath.getText());
        meta.setDynamicSourceFileNameField(wSourceFileNameField.getText());
        meta.setDynamicTargetFileNameField(wTargetFileNameField.getText());
        meta.setErrorMessageField(wErrorMessageField.getText());
        meta.setDeleteSource(wDeleteSource.getSelection());
        meta.setCreateParentFolder(wCreateParentFolder.getSelection());
        meta.setOverwriteTarget(wOverwriteTarget.getSelection());
        stepname = wStepname.getText();

        dispose();
    }

    private void get() {
        if (!gotPreviousFields) {
            gotPreviousFields = true;
            try {
                String source = wSourceFileNameField.getText();
                String target = wTargetFileNameField.getText();
                RowMetaInterface r = transMeta.getPrevStepFields(stepname);

                wSourceFileNameField.removeAll();
                wTargetFileNameField.removeAll();

                if (r != null) {
                    wSourceFileNameField.setItems(r.getFieldNames());
                    wTargetFileNameField.setItems(r.getFieldNames());
                    
                    if (source != null) {
                        wSourceFileNameField.setText(source);
                    }

                    if (target != null) {
                        wTargetFileNameField.setText(target);
                    }
                }
            } catch (KettleException ke) {
                new ErrorDialog(shell,
                    BaseMessages.getString(PKG, "ConvertImagesDialog.FailedToGetFields.DialogTitle"),
                    BaseMessages.getString(PKG, "ConvertImagesDialog.FailedToGetFields.DialogMessage"),
                    ke);
            }
        }
    }
}
