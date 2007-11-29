package
{
	import flash.display.Sprite;
	import flash.display.MovieClip;
	import flash.display.StageAlign;
	import flash.display.StageScaleMode;
	import flash.display.Bitmap;
	import flash.events.Event;
	import flash.text.TextField;
	
	
	/* Default framerate will be 15 fps */
	[SWF(width="565", height="425", frameRate="15", backgroundColor="#AAAAAA")]
	
	public class Main extends Sprite
	{
		[Embed(source="flvPlayer.swf", symbol="ControlBar")]
		[Bindable]
		public var ControlBar:Class;

		[Embed(source="flvPlayer.swf", symbol="linkIndicator")]
		[Bindable]
		public var LinkIndicator:Class;

		[Embed(source="flvPlayer.swf", symbol="nextButton")]
		[Bindable]
		public var NextButton:Class;

		[Embed(source="flvPlayer.swf", symbol="prevButton")]
		[Bindable]
		public var PreviousButton:Class;

		[Embed(source="flvPlayer.swf", symbol="slower")]
		[Bindable]
		public var SlowButton:Class;

		[Embed(source="flvPlayer.swf", symbol="aslpahButton")]
		[Bindable]
		public var ASLpahButton:Class;

		[Embed(source="flvPlayer.swf", symbol="TextButton")]
		[Bindable]
		public var TextButton:Class;

		[Embed(source="flvPlayer.swf", symbol="thumbnailLink")]
		[Bindable]		
		public var ThumbNailLink:Class;
		
		[Embed(source="flvPlayer.swf", symbol="rightButton")]
		[Bindable]
		public var RightButton:Class;

		[Embed(source="flvPlayer.swf", symbol="leftButton")]
		[Bindable]
		public var LeftButton:Class;
		
		private var video			:FlvPlayer;
		private var videoController	:VideoController;
		public var controlBar		:Sprite;
		//private var times			:Array;
		private var playerBCK		:Sprite;
		public var linkIndicator	:Bitmap;	// (red) frame behind the player
		public var leftControlBar	:Sprite;
		public var nextButt			:Sprite;
		public var prevButt			:Sprite;
		public var slowButt			:Sprite;
		public var aslpahButt		:Sprite;
		public var textButt			:Sprite;
		private var xmlData			:XMLData;
		public var field			:TextField = new TextField();
		public var linksHolder		:Sprite;
		private var signLinksArray	:Array;
		public var leftArrow		:Sprite;
		public var rightArrow		:Sprite;
		private var xOffSet			:int = 31;
		public var shiftIndex		:int;		// the leftmost visible link after shift
		public var debugTA			:TextField = new TextField();
		private var debugEnabled	:Boolean = true;
		public var currentSL		:int = 0;
		
		public function Main()
		{
			this.initialize();
		}
				
		public function initialize():void
		{
			stage.scaleMode = StageScaleMode.NO_SCALE;
			stage.align=StageAlign.TOP_LEFT;

			xmlData 		= new XMLData();
			controlBar		= new ControlBar();
			linkIndicator 	= new LinkIndicator();
			//thumbnailLink	= new ThumbNailLink();
			signLinksArray	= new Array(xmlData.getNumLinks());
			shiftIndex = 0;
			
			initializeTrack();
			
			/* adds the player's control bar to the stage */
			this.addChild(controlBar);
			controlBar.x = 31;
			controlBar.y = 268;
			
			/* sets up the control bar on the left */
			leftControlBar = new Sprite();
			leftControlBar.graphics.lineStyle(1,0xAAAAAA);
			leftControlBar.graphics.beginFill(0xAAAAAA);
			leftControlBar.graphics.drawRect(0,0,31,320);
			leftControlBar.graphics.endFill();
			this.addChild(leftControlBar);

			nextButt	= new NextButton();		nextButt.buttonMode = true;
			prevButt	= new PreviousButton();	prevButt.buttonMode = true;
			slowButt 	= new SlowButton();		slowButt.buttonMode = true;
			aslpahButt 	= new ASLpahButton();	aslpahButt.buttonMode = true;
			textButt 	= new TextButton();		textButt.buttonMode = true;

			leftControlBar.addChild(prevButt);
			prevButt.x = 4;
			prevButt.y = 10;

			leftControlBar.addChild(nextButt);
			nextButt.x = 4;
			nextButt.y = 35;

			leftControlBar.addChild(slowButt);
			slowButt.x = 4;
			slowButt.y = 70;

			leftControlBar.addChild(aslpahButt);
			aslpahButt.x = 4;
			aslpahButt.y = 100;

			leftControlBar.addChild(textButt);
			textButt.x = 4;
			textButt.y = 130;

			/* sets up the player's background: black initially */
			playerBCK = new Sprite();
			playerBCK.graphics.lineStyle(1,0x000000);
			playerBCK.graphics.beginFill(0x000000);
			playerBCK.graphics.drawRect(0,0,325,263);
			playerBCK.graphics.endFill();
			this.addChild(playerBCK);
			playerBCK.x = 31;
			playerBCK.y = 0;
			
			playerBCK.addChildAt(linkIndicator,0);
			linkIndicator.x = 0;
			linkIndicator.y = 0;
			linkIndicator.visible = false;		

			/* sets up the holder and navigational arrows for "SignLinks" 
			 * height: 120, width: 357, usable: 357-xOffSet = 326
			 * set arrows
			 */			
			linksHolder = new Sprite();
			linksHolder.graphics.lineStyle(2,0xCCCCCC);
			linksHolder.graphics.beginFill(0xCCCCCC);
			linksHolder.graphics.drawRect(0,0,357,120);
			linksHolder.graphics.endFill();
			
			this.addChild(linksHolder);
			linksHolder.x = 0;
			linksHolder.y = 290;			
 
			leftArrow  = new LeftButton(); 	leftArrow.buttonMode = true; 	leftArrow.useHandCursor = true;
			rightArrow = new RightButton();	rightArrow.buttonMode = true;	rightArrow.useHandCursor = true;
			
			leftArrow.x = 3;	leftArrow.y = 100;
			rightArrow.x = 333;	rightArrow.y = 100;
			
			linksHolder.addChild(leftArrow);
			linksHolder.addChild(rightArrow);
			
			leftArrow.x = xOffSet + 1;	leftArrow.y = 5;
			rightArrow.x = 357-15-1;	rightArrow.y = 5;
			
			linksHolder.addChild(leftArrow);
			linksHolder.addChild(rightArrow);

			
			this.addSL();
			
			//videoController = new VideoController(xmlData.getMovieName(), xmlData.getTimesArray()); //use this later with ffmpeg to output the flv file
			videoController = new VideoController(xmlData.getMovieName(), xmlData.getTimesArray());
			playerBCK.addChild(videoController);

			/* sets up the textbox on the right */
			this.addChild(field); // field is the textfield
			field.x 				= 360;
			field.y 				= 0;
			field.width 			= 200;
			field.height 			= 285;
			field.multiline 		= true;
			field.border 			= true;
			field.borderColor 		= 0xAAAAAA;	//make the border gray
			field.background 		= true;
			field.backgroundColor 	= 0xCCCCCC;	//make the background lighter gray
			field.htmlText			= xmlData.getHtmlText();
			field.visible = false;
		}
		
		/* gets the "SignLinks" and adds them on stage */
		private function addSL():void
		{
			for (var i:int=0; i<xmlData.getNumLinks(); i++)	// i=0..4
			{
				/*	SignLink(thumb:Bitmap, sTime:Number, eTime:Number, link:String, linkLabel:String="") */
				signLinksArray[i] = new SignLink(xmlData.getImagesArray()[i],xmlData.getTimesArray()[2*i],xmlData.getTimesArray()[2*i+1],xmlData.getLinksArray()[i],xmlData.getLabelsArray()[i]);
				signLinksArray[i].setSLinkID(i);
				linksHolder.addChild(signLinksArray[i]);
				
				if(i > 2)
				{
					signLinksArray[i].visible = false;
				}
				signLinksArray[i].x = xOffSet+17;
				signLinksArray[i].y = 5;				
				xOffSet+=98;
				//currentSL
			}
		}
		
		// debugging function
		public function debug(debugText:String):void
		{
			if (debugEnabled)
			{
				this.addChild(debugTA);
				this.removeChild(debugTA);
				debugTA.x = 360;
				debugTA.y = 10;
				debugTA.height = 400;
				debugTA.width = 200;
				debugTA.multiline = true;
				debugTA.replaceSelectedText(debugText + "\n");
				//debugTA.appendText(debugText+"\n");
				this.addChild(debugTA);
			}
		}
		
		public function shiftToStart():void
		{
			xOffSet = 31;
			shiftIndex = 0;
			for (var i:int=0; i<xmlData.getNumLinks(); i++)
			{				
				if(i > 2)
				{
					signLinksArray[i].visible = false;
				}else{
					signLinksArray[i].visible = true;
					signLinksArray[i].x = xOffSet+17;
					xOffSet+=98;
				}
			}
		}
		
		public function shiftToEnd():void
		{
			xOffSet = 31;
			shiftIndex = xmlData.getNumLinks() - 3;
			for (var i:int=0; i<xmlData.getNumLinks(); i++)
			{				
				if(i < xmlData.getNumLinks()-3)
				{
					signLinksArray[i].visible = false;
				}else{
					signLinksArray[i].visible = true;
					signLinksArray[i].x = xOffSet+17;
					xOffSet+=98;
				}
			}
		}
		
		public function shiftToMiddle(curIndex:int):void
		{
			if (curIndex == shiftIndex+1) { debug("shiftIndex + 1 = curIndex = " + curIndex); return;}
			else if (curIndex == shiftIndex) 
			{
				shiftLeft(); return;
			}
			else if (curIndex == shiftIndex+2) 
			{
				shiftRight(); return;
			}
		}
		
		public function shiftLeft():void
		{
			xOffSet = 31;
			// check whether shift is possible
			if(shiftIndex <= 0){ return; }
			if(shiftIndex > 0)
			{
				shiftIndex--;
				for (var i:int=0; i<xmlData.getNumLinks(); i++)
				{				
					// hide all signlinks on the left
					if (i < shiftIndex){ signLinksArray[i].visible = false; }
					// hide all signlinks on the right
					else if (i > shiftIndex+2){ signLinksArray[i].visible = false; }
					else{
						signLinksArray[i].visible = true;
						signLinksArray[i].x = xOffSet+17;
						xOffSet+=98;
					}
				} // for loop 				
			}
			debug("Shift Left: " + shiftIndex.toString());
		}
		
		public function shiftRight():void
		{			
			xOffSet = 31;
			
			// check whether shift is possible
			// shiftIndex = last sLink, starts at 0
			if(shiftIndex+1 > xmlData.getNumLinks() - 3){ return; }
			if(shiftIndex+1 <= xmlData.getNumLinks() - 3)
			{
				shiftIndex++;
				for (var i:int=0; i < xmlData.getNumLinks(); i++)
				{				
					// hide all signlinks on the left
					if (i < shiftIndex){ signLinksArray[i].visible = false; }
					// hide all signlinks on the right
					else if (i > shiftIndex+2){ signLinksArray[i].visible = false; }
					else{
						signLinksArray[i].visible = true;
						signLinksArray[i].x = xOffSet+17;
						xOffSet+=98;
					}
				}
			}
			debug("Shift Right: " + shiftIndex.toString());
		}
		
		public function getSignLinksArray():Array{
			return signLinksArray;
		}		
		public function getVController():VideoController{
			return videoController;
		}
		
		public function drawLinkInterval(x:uint, width:uint, active:Boolean = false):void
		{
			if (active)
			{
				(controlBar.getChildByName("density")  as  MovieClip).slider.track.graphics.lineStyle(1,0xff0000);
				(controlBar.getChildByName("density")  as  MovieClip).slider.track.graphics.beginFill(0xff0000);
			}
			else
			{
				(controlBar.getChildByName("density")  as  MovieClip).slider.track.graphics.lineStyle(1,0x0000ff);
				(controlBar.getChildByName("density")  as  MovieClip).slider.track.graphics.beginFill(0x0000ff);
			}
			(controlBar.getChildByName("density")  as  MovieClip).slider.track.graphics.drawRect(x,1,width-1,(controlBar.getChildByName("density") as MovieClip).slider.track.height-2);
			(controlBar.getChildByName("density")  as  MovieClip).slider.track.graphics.endFill();
		}
		
		public function initializeTrack():void
		{
			(controlBar.getChildByName("density")  as  MovieClip).slider.track.graphics.lineStyle();
			(controlBar.getChildByName("density")  as  MovieClip).slider.track.graphics.beginFill(0xbbbbbb);
			(controlBar.getChildByName("density")  as  MovieClip).slider.track.graphics.drawRect(0,0,(controlBar.getChildByName("density") as MovieClip).slider.track.width,(controlBar.getChildByName("density") as MovieClip).slider.track.height);
			(controlBar.getChildByName("density")  as  MovieClip).slider.track.graphics.endFill();
			trace((controlBar.getChildByName("density") as MovieClip).slider.track);//.slider.track.height);
		}
	}//Main class
}//package