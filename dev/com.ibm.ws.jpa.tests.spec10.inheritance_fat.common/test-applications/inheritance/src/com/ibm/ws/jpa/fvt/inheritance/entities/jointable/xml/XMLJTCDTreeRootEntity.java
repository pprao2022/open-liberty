/*******************************************************************************
 * Copyright (c) 2020, 2021 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package com.ibm.ws.jpa.fvt.inheritance.entities.jointable.xml;

import com.ibm.ws.jpa.fvt.inheritance.entities.ITreeRoot;

public abstract class XMLJTCDTreeRootEntity implements ITreeRoot {
    private int id;

    private String name;

    public XMLJTCDTreeRootEntity() {
        super();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "XMLJTCDTreeRootEntity [id=" + id + ", name=" + name + "]";
    }
}
