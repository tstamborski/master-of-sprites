#!/bin/bash

jpackage --input MasterofSprites/dist --main-jar MasterofSprites.jar \
	--main-class com.tstamborski.masterofsprites.MasterofSprites \
	--name "MasterofSprites" \
	--type deb --app-version 1.0 \
	--copyright "Copyright (c) 2025 Tobiasz Stamborski" \
	--description "Java editor for Commodore 64 sprites." \
	--icon commodore-puppet48.png --vendor "Tobiasz Stamborski" \
	--about-url "https://github.com/tstamborski/master-of-sprites" \
	--install-dir /usr --license-file MasterofSprites/src/com/tstamborski/masterofsprites/docs/license.txt \
	--linux-shortcut --linux-menu-group Graphics 

