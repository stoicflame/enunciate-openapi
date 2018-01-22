/*
 * Copyright © 2017-2018 Jyske Bank
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.jyskebank.tools.enunciate.modules.openapi.yaml;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Printer helping keep track of YAML indentation levels.
 */
public class IndententationPrinter {
  private static final String DOS_NEWLINE = "\r\n";
  private static final String SEQUENCE_PREFIX = "- ";
  private final StringBuilder sb = new StringBuilder();
  private Deque<IndentLevel> indentLevelStack = new ArrayDeque<>();
  private Deque<IndentLevel> memoryStack = new ArrayDeque<>();
  private boolean itemFollows;

  public IndententationPrinter(String initialIndentation) {
    indentLevelStack.push(new IndentLevel(initialIndentation));
  }

  private IndentLevel getActive() {
    return indentLevelStack.peek();
  }

  public void itemFollows() {
    this.itemFollows = true;
  }
  
  public IndententationPrinter item(String... lines) {
    itemFollows = true;
    return add(lines);
  }

  public IndententationPrinter add(String... lines) {
    addIndentationForAllButFirstLine();
    for (String line : lines) {
      sb.append(line);
    }
    return this;
  }

  private void addIndentationForAllButFirstLine() {
    if (sb.length() != 0) {
      String indent = getActive().indentation;
      if (itemFollows) {
        indent = indent.replaceAll("  $", SEQUENCE_PREFIX);
        itemFollows = false;
      }
      sb.append(DOS_NEWLINE).append(indent);
    }
  }
  
  public IndententationPrinter nextLevel() {
    indentLevelStack.push(getActive().next());
    return this;
  }

  public IndententationPrinter prevLevel() {
    if (indentLevelStack.size() == 1) {
      throw new IllegalStateException("Cannot go beyond initial indentation");
    }
    indentLevelStack.pop();
    return this;
  }

  public IndententationPrinter pushNextLevel() {
    memoryStack.push(getActive());
    return nextLevel();
  }
  
  public IndententationPrinter popLevel() {
    IndentLevel revertTo = memoryStack.pop();
    while (indentLevelStack.peek() != revertTo) {
      indentLevelStack.pop();
    }
    return this;
  }
  
  public String toString() {
    return sb.toString();
  }

  public static class IndentLevel {
    private static final String INDENT_PREFIX = "  ";

    public final String indentation;
    
    public IndentLevel(String indentation) {
      this.indentation = indentation;
    }
    
    public IndentLevel next() {
      return new IndentLevel(indentation.replace('-', ' ') + INDENT_PREFIX);
    }
  }
}
