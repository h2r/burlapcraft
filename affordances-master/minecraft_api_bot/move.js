var fs = require('fs');
var mineflayer = require('../minecraft_api/mineflayer/');
var vec3 = mineflayer.vec3;
// var furnace = mineflayer.furnace;
var bot = mineflayer.createBot({
  username: "aye_priori",
    password: "password",
});


MAXBLOCKS = 2;

goalX = 143;
goalY = 72;
goalZ = 960;

blockNum = MAXBLOCKS;

actionKillSwitch = false;

bot.on('chat', function(username, message) {
  actionKillSwitch = false;
  switch (message)
  {
    case "F":
      move("forward");
      break;
    case "B":
      move("back");
      break;
    case "L":
      move("left");
      break;
    case "R":
      move("right");
      break;
    case "loc":
      logBotLoc();
      break;
    case "openD":
      openDoor();
      break;
    case "give":
      bot.chat("/give aye_priori 35 64");
      bot.chat("/give aye_priori 278");
      break;
    case "gm0":
      bot.chat("/gamemode 0");
      break;
    case "bake":
      bakeBread();
      break;    
    case "r":
      bot.chat("/tp aye_priori 283 87 999");
      bot.chat("/time set 1000");
      bot.chat("/weather clear 99999");
      actionKillSwitch = true;
      blockNum = MAXBLOCKS;
      orient();
      break;
    case "half":
      bot.chat("/tp aye_priori 289.5 87 997.5");
      bot.chat("/time set 1000");
      bot.chat("/weather clear 99999");
      actionKillSwitch = true;
      blockNum = MAXBLOCKS;
      orient();
      break;
    case "or":
      bot.chat("/tp aye_priori 155.3 84.0 922.5");
      bot.chat("/time set 10000");
      actionKillSwitch = true;
      blockNum = MAXBLOCKS;
      bot.look(0, 0);
      break;
    case "er":
      bot.chat("/tp aye_priori 149.3 72.0 957.5");
      bot.chat("/time set 10000");
      actionKillSwitch = true;
      blockNum = MAXBLOCKS;
      bot.look(0, 0);

      break;
    case "reset":
      bot.chat("/tp aye_priori 150.7 74.0 968.0");
      bot.chat("/time set 10000");
      actionKillSwitch = true;
      blockNum = MAXBLOCKS;
      break;
    case "destroy":
      destroy("forward");
      break;
    case "grain":
      pickUpGrain();
      break;
    case "placeF":
      place("forward");
      break;
    case "placeB":
      place("back");
      break;
    case "placeL":
      place("left");
      break;
    case "placeR":
      place("right");
      break;
    default:
      if (message.slice(0,4) == "plan") {
        planNum = message.slice(4);
        plan = parsePlan(planNum);
        execPlan(plan, 0);
      }
  } 
});

function logBotLoc() {
  botMessage = "curLoc = ( "  + String(bot.entity.position.x) + ", " + String(bot.entity.position.y) + ", " + String(bot.entity.position.z) + " )";
  console.log(botMessage);
}

function orient() {
  bot.look(0, 0);
}

function isInGoalState() {

  // For readability
  botX = bot.entity.position.x;
  botY = bot.entity.position.y;
  botZ = bot.entity.position.z;

  if (Math.abs(botX - goalX) < 0.6 && Math.abs(botZ - goalZ) < 0.6) {
    // console.log("WOOHOO I MADE IT! :)");
    bot.chat("WOOHOO I MADE IT! :)");
    return true;
  }
  else {
    return false;
  }

}

// Takes as input a world number and returns a list (of strings) of steps to execute
function parsePlan(worldNum) {

  var lines = fs.readFileSync('plan_world' + worldNum + '.p', 'utf8').split(',');
  var arr = new Array();
  for (var l in lines){
      var line = lines[l];
      arr.push(line);
  }
  actionKillSwitch = false;
  return arr;
}

function execPlan(plan, step) {
  // set curr step control state to true
  orient();

  if (isMoveCommand(plan[step])) {
    bot.setControlState(plan[step], true)
  } else if (plan[step] == "door") {
    openDoor();
  } else if (plan[step] == "jumpF") {
    jump("forward");
  } else if (plan[step] == "jumpB") {
    jump("back");  
  } else if (plan[step] == "jumpL") {
    jump("left");
  } else if (plan[step] == "jumpR") {
    jump("right");  
  } else if (plan[step] == "placeF") {
    place("forward");
  } else if (plan[step] == "placeB") {
    place("back");
  } else if (plan[step] == "placeL") {
    place("left");
  } else if (plan[step] == "placeR") {
    place("right");
  } else if (plan[step] == "destroyF") {
    destroy("forward");
  } else if (plan[step] == "destroyB") {
    destroy("back");
  } else if (plan[step] == "destroyL") {
    destroy("left");
  } else if (plan[step] == "destroyR") {
    destroy("right");
  } else if (plan[step] == "pickUpGrain") {
    pickUpGrain();
  } else if (plan[step] == "bakeBread") {
    bakeBread();
  }

  // Clear control states
  setTimeout(function() {bot.clearControlStates();}, 275)

  // Celebrate and peace out if in goal state
  if (isInGoalState()) {
    return;
  }

  // if not at end of plan, setInterval for next step
  if (step < plan.length) {
    setTimeout(function() {execPlan(plan, step + 1);}, 800)
  }
}

function pickUpGrain() {
  equip("pickaxe")
  b1 = bot.blockAt(bot.entity.position.offset(1,0,0));
  b2 = bot.blockAt(bot.entity.position.offset(-1,0,0));
  b3 = bot.blockAt(bot.entity.position.offset(0,0,1));
  b4 = bot.blockAt(bot.entity.position.offset(0,0,-1));

  if (b1.name == "oreGold") {
    destroy("right");
  } else if (b2.name == "oreGold") {
    destroy("left");
  } else if (b3.name == "oreGold") {
    destroy("forward");
  } else if (b4.name == "oreGold") {
    destroy("back");
  } else {
    console.log("Not near grain " + bot.entity.position);
  }
}

function bakeBread() {
  equip("ore")

  b1 = bot.blockAt(bot.entity.position.offset(1,0,0));
  b2 = bot.blockAt(bot.entity.position.offset(-1,0,0));
  b3 = bot.blockAt(bot.entity.position.offset(0,0,1));
  b4 = bot.blockAt(bot.entity.position.offset(0,0,-1));

  if (b1.name == "furnace") {
    watchFurnace(b1);
    } else if (b2.name == "furnace") {
    watchFurnace(b2);
  } else if (b3.name == "furnace") {
    watchFurnace(b3);
  } else if (b4.name == "furnace") {
    // bot.openFurnace(b4);
    watchFurnace(b4);
  } else {
    console.log("Not near furnace " + b1.name + " " + b2.name + " " + b3.name + " " + b4.name);
  }
}

function itemStr(item) {
  if (item) {
    return item.name + " x " + item.count;
  } else {
    return "(nothing)";
  }
}

 function watchFurnace(furnaceBlock) {
  if (! furnaceBlock) {
    bot.chat("no furnace found");
    return;
  }
  var furnace = bot.openFurnace(furnaceBlock);
  furnace.on('open', onChat);

  
  function onChat(username, message) {
    var words, cmd, name, amount, item, fn;
    message = "input 1"
    if (message === 'close') {
      furnace.close();
      bot.removeListener('chat', onChat);
    } else if (/^(input|fuel) (\d+)/.test(message)) {
      words = message.split(/\s+/);
      cmd = words[0];
      amount = parseInt(words[1], 10);
      name = words[2];
      item = itemByName("oreGold");
      if (!item) {
        bot.chat("unknown item " + name);
        return;
      }
      fn = cmd === 'input' ? furnace.putInput : furnace.putFuel;
      fn.call(furnace, item.type, null, amount, function(err) {
        if (err) {
          bot.chat("unable to put " + amount + " " + item.name);
        } else {
          // bot.chat("put " + amount + " " + item.name);
        }
      });
    } else if (/^take (input|fuel|output)$/.test(message)) {
      words = message.split(/\s+/);
      cmd = words[1];
      fn = {
        input: furnace.takeInput,
        fuel: furnace.takeFuel,
        output: furnace.takeOutput,
      }[cmd];
      fn.call(furnace, function(err, item) {
        if (err) {
          bot.chat("unable to take " + item.name + " from " + cmd);
        } else {
          bot.chat("took " + item.name + " " + cmd);
        }
      });
    }
  }
}


function jump(dir) {
  bot.setControlState(dir, true);
  bot.setControlState("jump", true);
  setTimeout(function() {bot.clearControlStates();}, 90)

}

function isMoveCommand(action) {
  console.log("[" + action + "]")
  return (action == "right" || action == "left" || action == "forward" || action == "back");
}

function openDoor() {
  rightBlock = bot.blockAt(bot.entity.position.offset(1, 0, 0))
  leftBlock = bot.blockAt(bot.entity.position.offset(-1, 0, 0))
  downBlock = bot.blockAt(bot.entity.position.offset(0, 0, 1))
  upBlock = bot.blockAt(bot.entity.position.offset(0, 0, -1))

  console.log("r: " + rightBlock.name)
  console.log("r: " + upBlock.name)
  console.log("r: " + downBlock.name)
  console.log("r: " + leftBlock.name)

  if (leftBlock.name == "doorWood") {
    bot.activateBlock(leftBlock);
  }
  else if (rightBlock.name == "doorWood") {
    bot.activateBlock(rightBlock);
  }
  else if (upBlock.name == "doorWood") {
    bot.activateBlock(upBlock);
  }
  else if (downBlock.name == "doorWood") {
    bot.activateBlock(downBlock);
  }

  bot.look(0, 0);

}


function canMakeMove(moveDir) {
  var dx = 0;
  var dz = 0;

  // Add 1.5 (instead of 1) here as a hack to make sure we're checking the next block (MC coordinates are extremely imprecise..)
  switch (moveDir)
  {
    case "forward":
      dz = -1;
      break;
    case "back":
      dz = 1;
      break;
    case "left":
      dx = -1;
      break;
    case "right":
      dx = 1;
      break;
  }

  if (bot.blockAt(bot.entity.position.offset(dx, 0, dz)).name != "air" || bot.blockAt(bot.entity.position.offset(dx, -1, dz)).name == "air")  {
    // Movement obstructed or there is a hole
    return false;
  }
  return true;

}

function isBotCommand(cmd) {
// Takes in a string from the agents plan
// returns True if the string corresponds to a movement command and FALSE otherwise (for now, later will add place and dig)
  if (cmd == "forward" || cmd == "back" || cmd == "left" || cmd == "right" || cmd == "placeForward") {
    return true;
  }
  else {
    return false;
  }
}

function move(dir) {
  
  bot.clearControlStates();
  bot.setControlState(dir, true);

  var startX = bot.entity.position.x;
  var startY = bot.entity.position.y;
  var startZ = bot.entity.position.z;

  bot.on('move', movedOne);
  
  function movedOne() {
    if (Math.abs(bot.entity.position.x - startX) >= 1 || Math.abs(bot.entity.position.y - startY) >= 1 || Math.abs(bot.entity.position.z - startZ) >= 1) {
      console.log(isInGoalState());
      bot.setControlState(dir, false);
      bot.removeListener('move', movedOne);
    }
  }
}

function destroy(dir) {
  equip("pickaxe")
  if (dir == "forward") {
    dx = 0
    dy = 0
    dz = -1
  }
  else if (dir == "back") {
    dx = 0
    dy = 0
    dz = 1
  }
  else if (dir == "right") {
    dx = 1
    dy = 0
    dz = 0
  }
  else if (dir == "left") {
    dx = -1
    dy = 0
    dz = 0
  }

  destroyBlock = bot.blockAt(bot.entity.position.offset(dx,dy ,dz));
  console.log("destroyingBOT: [" + destroyBlock.name + "]");
  if (bot.canDigBlock(destroyBlock)) {
    bot.dig(destroyBlock);

    topBlock = bot.blockAt(bot.entity.position.offset(dx,dy + 1,dz));
    
    setTimeout(function() {
      if (bot.canDigBlock(topBlock)) {
        bot.dig(topBlock);
        console.log("destroyingTOP: [" + topBlock.name + "]");
      }
    }, 700)


  }
  return;
}

function equip(itemName) {
  // 1 = pick
  // 2 = placeBlock
  console.log(bot.inventory.slots.length);
  // Pickaxe
  if (itemName == "pickaxe") {
    item = itemByName("pickaxeDiamond");
    bot.equip(item,'hand',null);
  } else if (itemName == "block") {
    item = itemByName("cloth");
    bot.equip(item,'hand',function(err){});
  } else if (itemName == "ore") {
    item = itemByName("oreGold");
    bot.equip(item,'hand',function(err){});
  }
}

function itemByName(name) {
  return bot.inventory.items().filter(function(item) {
    return item.name === name;
  })[0];
}

function place(dir) {
  // dir = {"left", "right", "forward", "back"}
  // dist = {1, 2, 3, 4} // TODO: use dist.
  equip("block")

  // Bot doesn't have any more blocks!
  if (blockNum  <= 0) {
    return;
  }

  dx = 0;
  dy = 0; // TODO: more elegant way of figuring out dy
  dz = 0;

  if (dir == "left")
    dx = -1.1
  else if (dir == "right")
    dx = 1.1
  else if (dir == "forward")
    dz = -1.1
  else if (dir == "back")
    dz = 1.1

  // Places a block on top of the block directly in front of it.
  placeBlock = bot.blockAt(bot.entity.position.offset(dx,dy,dz));

  console.log(dx,dy)

  // Finds the first non air block in this coordinate "column" that is within the bots "reach" (3)
  while (placeBlock.name == "air") {
    dy = dy - 1;
    placeBlock = bot.blockAt(bot.entity.position.offset(dx,dy,dz));
    if (dy == -2) {
      break;
    }
  }

  x = bot.entity.position.offset(dx,dy,dz)["x"]
  y = bot.entity.position.offset(dx,dy + 1,dz)["y"] // Gets the actual coordinate of the block to be changed 
  z = bot.entity.position.offset(dx,dy,dz)["z"]


  // bot.on('blockUpdate', function(point) {} );

    // Todo: remove this listener
  // bot.on('blockUpdate(' + x + ',' + y + ',' + z + ')',function () {console.log("howdy");}, true);

  // Always placing on the top face (for now)
  placeVec = vec3(placeBlock.position.x, placeBlock.position.y + 1, placeBlock.position.z)
  bot.placeBlock(placeBlock, placeVec);

  blockNum = blockNum - 1;
  return [dx, dy, dz];
}
