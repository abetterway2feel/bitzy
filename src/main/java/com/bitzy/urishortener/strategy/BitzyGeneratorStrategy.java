package com.bitzy.urishortener.strategy;

import java.util.Iterator;

import com.bitzy.urishortener.model.Bitzy;

public interface BitzyGeneratorStrategy extends Iterator<Bitzy>{

  public Bitzy next();
  public boolean hasNext();
}
