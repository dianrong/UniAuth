package com.dianrong.loanbusiness.subsystem.test;

import org.junit.Before;
import org.junit.Test;

import com.dianrong.loanbusiness.subsystem.MessageService;

import static org.junit.Assert.assertEquals;

public class MessageServiceTest {

  private MessageService messageService;

  @Before
  public void setUp() {
    messageService = new MessageService();
  }

  @Test
  public void getMessage_ShouldReturnMessage() {
    assertEquals("Hello World!", messageService.getMessage());
  }
}
