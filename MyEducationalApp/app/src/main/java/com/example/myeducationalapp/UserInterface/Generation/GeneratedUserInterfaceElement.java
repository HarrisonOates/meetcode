package com.example.myeducationalapp.UserInterface.Generation;

import androidx.constraintlayout.widget.ConstraintLayout;

public abstract class GeneratedUserInterfaceElement {

    ConstraintLayout parentReference;

    public abstract GeneratedUserInterfaceElement generate();

    public ConstraintLayout getParentReference() {
        return parentReference;
    }

    public void setParentReference(ConstraintLayout parentReference) {
        this.parentReference = parentReference;
    }
}
