function findElem(sectionId, elementUrl) {
	var currElem = angular.element("#appShell").scope().content[sectionId];
	for(let i = 0; i < elementUrl.length; i++) {
		currElem = currElem[elementUrl[i]];
	}
	return currElem;
}

function changeTabState(args) {
	var argNames = Object.keys(args);
	if (argNames.length == 2 && argNames.includes('buttonUrl') 
		&& argNames.includes('sectionId')) {
		// Remove button segment of url
		var accordionUrl = args["buttonUrl"].slice(0, args["buttonUrl"].length - 1);
		var accordionTab = findElem(args["sectionId"], accordionUrl);

		// Invert open value and update button icon
		accordionTab.open = !accordionTab.open;
		var mainCtrlScope = angular.element("#appShell").scope();
		if (accordionTab.open) {
			accordionTab.expandButton.icon = mainCtrlScope.componentProp["accordBtnOpen"]["icon"];
		} else {
			accordionTab.expandButton.icon = mainCtrlScope.componentProp["accordBtnClosed"]["icon"];
		}
	}
}

function updateMajMinSliders(args) {
	var argNames = Object.keys(args);
	if (argNames.length == 5 && argNames.includes('selectUrl') 
		&& argNames.includes('sectionId') && argNames.includes('selectedGroups')
		&& argNames.includes('sliderGroupId') && argNames.includes('rangeProp')) {
		// Remove select segment of url
		var containerUrl = args["selectUrl"].slice(0, args["selectUrl"].length - 1);
		var container = findElem(args["sectionId"], containerUrl);

		// Select element that holds Min and Max representation sliders (for each selected group)
		var sliderGroup = container[args["sliderGroupId"]];
		var genProp = angular.element("#appShell").scope().componentProp;
		var sliderStruct = genProp[args["rangeProp"]];
		var labelSuffix = "Slider";
		// Compute difference between current sliders and selected ones
		var currLabels = Object.keys(sliderGroup.sections);
		var removeList= currLabels.filter(currLabel => 
			!args["selectedGroups"].includes(currLabel.substring(0, currLabel.length - labelSuffix.length))); 
		// Removing nonexistant group sliders
		angular.forEach(removeList, function(group, index) {
			delete this.sections[group+labelSuffix];
		}, sliderGroup);
		// Adding new group sliders
		angular.forEach(args["selectedGroups"], function(group, index) {
			var label = group+labelSuffix;
			if (!currLabels.includes(label)) {
				sliderStruct["id"] = "percent" + group;
				sliderStruct["label"] = group + "Percentage Range";
				var groupPercent = parseGui(sliderStruct, genProp);
	  			this.sections[label] = groupPercent;
			}
		}, sliderGroup);
	}
}

function parseGui(guiStructure, componentProp, url) {
	if (guiStructure && "type" in guiStructure) {
		switch(guiStructure["type"]) {
			case "guiObj":
				url.push(guiStructure["url"]);
				return new GuiObj(url, guiStructure["classes"], guiStructure["ifClause"], 
								  guiStructure["flex"], guiStructure["layout"]);
			break;
			case "textBtn":
				var properties = componentProp[guiStructure["properties"]];
				properties["url"] = url.slice();
				return new TextButton(properties, 
									  guiStructure["id"], guiStructure["label"], 
									  guiStructure["text"], guiStructure["clickEvent"], 
									  guiStructure["eventArgs"], guiStructure["tooltip"], 
									  guiStructure["tooltipDir"]);
			break;
			case "iconBtn":
				var properties = componentProp[guiStructure["properties"]];
				properties["url"] = url.slice();
				return new IconButton(properties, 
									  guiStructure["id"], guiStructure["label"], 
									  guiStructure["icon"], guiStructure["clickEvent"], 
									  guiStructure["eventArgs"], guiStructure["tooltip"], 
									  guiStructure["tooltipDir"]);
			break;
			case "numInput":
				var properties = componentProp[guiStructure["properties"]];
				properties["url"] = url.slice();
				return new NumberInput(properties, 
									   guiStructure["id"], guiStructure["label"], 
									   guiStructure["tooltip"], guiStructure["tooltipDir"], 
									   guiStructure["min"], guiStructure["max"], 
									   guiStructure["step"], guiStructure["value"], 
									   guiStructure["required"]);
			break;
			case "menu":
				let triggerButton = parseGui(guiStructure["triggerButton"], componentProp, url);
				let actions = [];
				guiStructure.actions.forEach(function(action) {
					actions.push(parseGui(action, componentProp, url));
				});
				
				var properties = componentProp[guiStructure["properties"]];
				return new Menu(properties, 
								guiStructure["open"], triggerButton, 
								actions, guiStructure["direction"]);
			break;
			case "slideMeasure":
				var properties = componentProp[guiStructure["properties"]];
				properties["url"] = url.slice();
				return new SliderMeasure(properties, 
									   guiStructure["id"], guiStructure["label"], 
									   guiStructure["tooltip"], guiStructure["tooltipDir"], 
									   guiStructure["min"], guiStructure["max"], 
									   guiStructure["step"], guiStructure["value"]);
			break;
			case "rangeMeasure":
				var properties = componentProp[guiStructure["properties"]];
				properties["url"] = url.slice();
				return new RangeMeasure(properties, 
									   guiStructure["id"], guiStructure["label"], 
									   guiStructure["tooltip"], guiStructure["tooltipDir"], 
									   guiStructure["min"], guiStructure["max"], 
									   guiStructure["step"], guiStructure["value"],
									   componentProp[guiStructure["childProperties"]]);
			break;
			case "guiGroup":
				var properties = componentProp[guiStructure["properties"]];
				properties["url"] = url.slice();

				let sections = {};
				url.push("sections");
				for (var sectionID in guiStructure["sections"]) {
					var section = guiStructure["sections"][sectionID];
					var sectionUrl = url.slice();
					sectionUrl.push(sectionID);
					sections[sectionID] = parseGui(section, componentProp, sectionUrl);
				}
				
				return new GuiGroup(properties, guiStructure["id"], sections);
			break;
			case "select":
				var properties = componentProp[guiStructure["properties"]];
				properties["url"] = url.slice();
				return new SelectDisplay(properties, 
									  guiStructure["id"], guiStructure["label"], 
									  guiStructure["options"], guiStructure["selected"],
									  guiStructure["multiSelect"], guiStructure["onChange"],
									  guiStructure["eventArgs"]);
			break;
			case "accordTab":
				var properties = componentProp[guiStructure["properties"]];
				properties["url"] = url.slice();

				let expandUrl = url.slice();
				expandUrl.push("expandButton");
				let expandProp = componentProp[guiStructure["expandButton"]];
				expandProp["id"] = properties["id"] + "Btn";
				let expandButton = parseGui(expandProp, componentProp, expandUrl);

				let contentUrl = url.slice();
				expandUrl.push("content");
				let content =  parseGui(guiStructure["content"], componentProp, contentUrl);
				return new AccordionTab(properties, 
									  guiStructure["id"], guiStructure["title"], 
									  content, guiStructure["flexOrder"],
									  guiStructure["open"], expandButton);
			break;
			default:
				return null;
		}
	} else {
		return null;
	}
}