package com.example.ninemenmorris.controller;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ninemenmorris.MainActivity;
import com.example.ninemenmorris.model.NineMenMorrisRules;
import com.example.ninemenmorris.view.MVCView;

public class MVCController extends AppCompatActivity {
    private NineMenMorrisRules nineMenMorrisRules;
    private MVCView mvcView;

    public MVCController(NineMenMorrisRules nineMenMorrisRules, MVCView mvcView) {
        this.nineMenMorrisRules = nineMenMorrisRules;
        this.mvcView = mvcView;
    }
}
