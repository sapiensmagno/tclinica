package br.com.tclinica.cucumber.stepdefs;

import br.com.tclinica.TclinicaApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = TclinicaApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
