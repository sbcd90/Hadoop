package com.sap.junits;

import org.junit.rules.ExternalResource;

public class NameContextResource extends ExternalResource {

  @Override
  protected void before() throws Throwable {
    NameContext nameContext = new NameContext("Subhobrata");
  }

  @Override
  protected void after() {
    super.after();
  }
}