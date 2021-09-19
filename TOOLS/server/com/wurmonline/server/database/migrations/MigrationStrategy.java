package com.wurmonline.server.database.migrations;

public interface MigrationStrategy {
  MigrationResult migrate();
  
  boolean hasPendingMigrations();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\database\migrations\MigrationStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */