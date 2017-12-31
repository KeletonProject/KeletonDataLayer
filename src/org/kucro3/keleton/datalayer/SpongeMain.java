package org.kucro3.keleton.datalayer;

import org.kucro3.keleton.implementation.KeletonInstance;
import org.kucro3.keleton.implementation.KeletonModule;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "keleton-datalayer",
        name = "keleton-datalayer",
        version = "1.0",
        description = "Keleton Data Layer",
        authors = "Kumonda221")
@KeletonModule(name = "keleton-datalayer",
               dependencies = "keletonframework")
public class SpongeMain extends KeletonInstance {
}
