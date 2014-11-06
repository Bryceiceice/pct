/*
 * Copyright  2000-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.phenix.pct;

import java.io.File;

import org.apache.tools.ant.BuildException;

/**
 * Loads data into database
 * 
 * @author <a href="mailto:g.querret+PCT@gmail.com">Gilles QUERRET</a>
 */
public class PCTLoadDataCallback extends PCTRun {
    private File srcFile = null;
    private String table = null;
    private String callback = null;
    private boolean append = true;

    public void setTable(String table) {
        this.table = table;
    }
    public void setCallback(String callback) {
        this.callback = callback;
    }
    
    public void setAppend(boolean append) {
        this.append = append;
    }

    /**
     * Input directory
     * 
     * @param srcDir directory
     */
    public void setSrcFile(File srcFile) {
        this.srcFile = srcFile;
    }

    /**
     * Do the work
     * 
     * @throws BuildException Something went wrong
     */
    public void execute() throws BuildException {

        if (getDbConnections().size() == 0) {
            cleanup();
            throw new BuildException(Messages.getString("PCTLoadData.0")); //$NON-NLS-1$
        }

        /*if (getDbConnections().size() > 1) {
            cleanup();
            throw new BuildException(Messages.getString("PCTLoadData.1")); //$NON-NLS-1$
        }*/

        if (srcFile == null) {
            cleanup();
            throw new BuildException(Messages.getString("PCTLoadData.2")); //$NON-NLS-1$
        }
        if (!srcFile.isFile()) {
            cleanup();
            throw new BuildException(Messages.getString("PCTLoadData.2")); //$NON-NLS-1$
        }

        addParameter(new RunParameter("srcdir", srcFile.getParentFile().getAbsolutePath()));
        addParameter(new RunParameter("filename", srcFile.getName()));
        addParameter(new RunParameter("tablename", table));
        addParameter(new RunParameter("append", Boolean.toString(append)));
        addParameter(new RunParameter("callbackClass", callback));
        try {
            setProcedure("pct/v11/loadData.p"); //$NON-NLS-1$
            super.execute();
            cleanup();
        } catch (BuildException be) {
            cleanup();
            throw be;
        }
    }
}
