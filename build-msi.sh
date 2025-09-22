#!/bin/bash

jpackage --input MasterofSprites/dist --main-jar MasterofSprites.jar \
	--main-class com.tstamborski.masterofsprites.MasterofSprites \
	--name "MasterofSprites" \
	--type msi --app-version 1.0 \
	--copyright "Copyright (c) 2025 Tobiasz Stamborski" \
	--description "Java editor for Commodore 64 sprites." \
	--icon commodore-puppet48.ico --vendor "Tobiasz Stamborski" \
	--about-url "https://github.com/tstamborski/master-of-sprites" \
	--license-file MasterofSprites/src/com/tstamborski/masterofsprites/docs/license.txt \
	--win-dir-chooser --win-menu --win-shortcut \
	--win-shortcut-prompt \
	--verbose

