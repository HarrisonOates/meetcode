package com.example.myeducationalapp.UserInterface.Generation;

import androidx.constraintlayout.widget.ConstraintLayout;

public abstract class GeneratedUserInterfaceElement {

    ConstraintLayout parent;

    public abstract GeneratedUserInterfaceElement generate();

    public void setParent(ConstraintLayout parentReference) {
        this.parent = parentReference;
    }
}
