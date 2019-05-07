class GuiObj {
	constructor(url, classes, ifClause, flex, layout) {
		this.url = url;
		this.classes = {};
		for (let i = 0; i < classes.length; i++){
			this.classes[classes[i]] = true;
		}

		this.ifClause = ifClause;
		this.flex = flex;
		this.layout = {};
		for (let i = 0; i < layout.length; i++){
			this.layout[layout[i]] = true;
		}

		this.type="guiObj";
	}

	packData() {
		return null;
	}
}

class TextButton extends GuiObj {
	constructor(properties, id, label, text, clickEvent, eventArgs, tooltip, tooltipDir) {
		super(properties.url, properties.classes, properties.ifClause, properties.flex, properties.layout);
		this.id = id; 
		this.label = label;
		this.text = text;
		this.clickEvent = clickEvent;
		this.eventArgs = eventArgs;
		this.tooltip = tooltip;
		this.tooltipDir = tooltipDir;
		this.type="textBtn";
	}
}

class IconButton extends GuiObj {
	constructor(properties, id, label, icon, clickEvent, eventArgs, tooltip, tooltipDir) {
		super(properties.url, properties.classes, properties.ifClause, properties.flex, properties.layout);
		this.id = id; 
		this.label = label;
		this.icon = icon;
		this.clickEvent = clickEvent;
		this.eventArgs = eventArgs;
		this.tooltip = tooltip;
		this.tooltipDir = tooltipDir;
		this.type="iconBtn";
	}
}

class NumberInput extends GuiObj {
	constructor(properties, id, label, tooltip, tooltipDir, min, max, step, value, required) {
		super(properties.url, properties.classes, properties.ifClause, properties.flex, properties.layout);
		this.id = id;
		this.label = label;
		this.tooltip = tooltip;
		this.tooltipDir = tooltipDir;
		this.min = min;
		this.max = max;
		this.step = step;
		this.value = value;
		this.required = required;
		this.type="numInput";
	}

	packData() {
		var result = {};
		result[this.id] = this.value;
		return result;
	}
}

class Menu extends GuiObj {
	constructor(properties, open, triggerButton, actions, direction) {
		super(properties.url, properties.classes, properties.ifClause, properties.flex, properties.layout);
		this.open = open;
		this.triggerButton = triggerButton;
		this.actions = actions;
		this.direction = direction;
		this.type="menu";
	}
}

class SliderMeasure extends GuiObj {
	constructor(properties, id, label, tooltip, tooltipDir, min, max, step, value) {
		super(properties.url, properties.classes, properties.ifClause, properties.flex, properties.layout);
		this.id = id;
		this.label = label;
		this.tooltip = tooltip;
		this.tooltipDir = tooltipDir;
		this.min = min;
		this.max = max;
		this.step = step;
		this.value = value;
		this.type="slideMeasure";
	}

	packData() {
		var result = {};
		result[this.id] = this.value;
		return result;
	}
}

class RangeMeasure extends SliderMeasure {
	constructor(properties, id, label, tooltip, tooltipDir, min, max, step, value, childProperties) {
		super(properties, id, label, tooltip, tooltipDir, min, max, step, value);

		this.lowID = this.id + "Low";
		this.lowSlider = new SliderMeasure(childProperties, this.lowID, "low", "", this.min, this.max, this.step, this.value);
		this.highID = this.id + "High";
		this.highSlider = new SliderMeasure(childProperties, this.highID, "high", "", this.min, this.max, this.step, this.value);
		this.type="rangeMeasure";
	}

	packData() {
		var low = this.lowSlider.packData()[this.lowID];
		var high = this.highSlider.packData()[this.highID];
		var result = {};
		result[this.id] = {"low": low, "high": high};
		return result;
	}
}

class GuiGroup extends GuiObj {
	constructor(properties, id, sections) {
		super(properties.url, properties.classes, properties.ifClause, properties.flex, properties.layout);
		this.id = id;
		this.sections = sections;
		this.type="guiGroup";
	}

	packData() {
		var packageData = {};
		for (const [sectionID, section] of Object.entries(this.sections)) {
			var subPack = section.packData();
			if (subPack != null) {
				var data;
				if (subPack.hasOwnProperty(sectionID)) {
					data = subPack[sectionID];
				}
				else {
					data = subPack;
				}
				packageData[sectionID] = data;
			}
		}

		var result = {};
		result[this.id] = packageData;
		return result;
	}
}

class SelectDisplay extends GuiObj {
	constructor(properties, id, label, options, selected, multiSelect, onChange, eventArgs) {
		super(properties.url, properties.classes, properties.ifClause, properties.flex, properties.layout);
		this.id = id;
		this.label = label;
		this.options = options;
		this.selected = selected;
		this.multiSelect = multiSelect;
		this.onChange = onChange;
		this.eventArgs = eventArgs;
		this.type="select";
	}

	packData() {
		var selectedVals = [];
		var valType = typeof this.selected;
		if (valType == "string") {
			selectedVals.push(this.selected);
		}
		else {
			selectedVals = selectedVals.concat(this.selected);
		}

		var result = {};
		result[this.id] = selectedVals;
		return result;
	}
}

class AccordionTab extends GuiObj {
	constructor(properties, id, title, content, flexOrder, open, expandButton) {
		super(properties.url, properties.classes, properties.ifClause, properties.flex, properties.layout);
		this.id = id;
		this.title = title;
		this.content = content;
		this.flexOrder = flexOrder;
		this.open = open;
		this.expandButton = expandButton;
		this.type="accordTab";
	}

	packData() {
		var data = this.content.packData()[this.content.id];
		if (Object.keys(data).length > 0) {
			var result = {};
			result[this.id] = data;
			return result;
		}
		else {
			return null;
		}
	}
}