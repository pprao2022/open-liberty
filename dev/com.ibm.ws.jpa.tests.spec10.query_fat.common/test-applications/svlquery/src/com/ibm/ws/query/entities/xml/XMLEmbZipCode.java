/*******************************************************************************
 * Copyright (c) 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package com.ibm.ws.query.entities.xml;

import javax.persistence.Embeddable;

import com.ibm.ws.query.entities.interfaces.IEmbZipCode;

@Embeddable
public class XMLEmbZipCode implements IEmbZipCode {
    protected String zip;
    protected String plusFour;

    public XMLEmbZipCode(String zip, String plusFour) {
        super();
        this.zip = zip;
        this.plusFour = plusFour;
    }

    @Override
    public String getZip() {
        return zip;
    }

    @Override
    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public String getPlusFour() {
        return plusFour;
    }

    @Override
    public void setPlusFour(String plusFour) {
        this.plusFour = plusFour;
    }

}
