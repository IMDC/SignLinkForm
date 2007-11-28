package signlinkform;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A listener class for Sign links manipulation buttons and menu items
 * 
 * @author Martin Gerdzhev
 * @version $Id: SignListener.java 53 2007-11-08 21:47:32Z martin $
 */
public class SignListener implements ActionListener
{
	private VideoComponent	vComp;
	private MenuFrame		menuFrame;

	public SignListener(MenuFrame mFrame)
	{
		vComp = mFrame.getVComponent();
		menuFrame = mFrame;
	}

	public void actionPerformed(ActionEvent event)
	{
		if (event.getActionCommand().equals("new sign"))
		{
			if (vComp.getVideoButtonsPanel().getPlayPauseIcon() == vComp.getVideoButtonsPanel().getPauseIcon())
			{
				vComp.getVideoButtonsPanel().getPlayPauseButton().doClick();
				//set the text box to the correct time
				vComp.getVideoButtonsPanel().getVText().setText(
						vComp.getVideoPanel().getMin() + ":" + vComp.getVideoPanel().getSec() + ":" + vComp.getVideoPanel().getMil());
			}
			vComp.getVideoPanel().setActive(false);
			menuFrame.validate();
			menuFrame.setEnabled(false);
			SignLink.getInstance(menuFrame).setVisible(true);
		}

		if (event.getActionCommand().equals("edit sign"))
		{
			int selected = menuFrame.getSComp().getSelected();
			if (selected > -1)
			{
				menuFrame.setEnabled(false);
				if (vComp.getVideoButtonsPanel().getPlayPauseIcon() == vComp.getVideoButtonsPanel().getPauseIcon())
				{
					vComp.getVideoButtonsPanel().getPlayPauseButton().doClick();
					//set the text box to the correct time
					vComp.getVideoButtonsPanel().getVText().setText(
							vComp.getVideoPanel().getMin() + ":" + vComp.getVideoPanel().getSec() + ":" + vComp.getVideoPanel().getMil());
				}
				vComp.getVideoPanel().setActive(false);
				SignLink aSign = SignLink
						.getInstance(menuFrame, menuFrame.getSComp().getSign(selected), menuFrame.getSComp().getSignList());
				aSign.setVisible(true);
				aSign.setEdited(true);
			}
			else
			{
				// print an error message. Button should not be enabled
				System.out.println("Error. No signlink is selected");
			}
		}

		if (event.getActionCommand().equals("clear sign"))
		{
			// TODO remove the textlinks corresponding to this signlink as well.
			int selected = menuFrame.getSComp().getSelected();
			if (selected > -1)
			{
				// remove the links with the id of the current signlink
				menuFrame.getTextListen().removeLinks(menuFrame.getSComp().getSign(selected).getMStart());

				menuFrame.setModified(true);

				menuFrame.getSComp().removeSign(selected);
				menuFrame.getSComp().getSButtonsPanel().getNewSignLinkButton().setEnabled(true);
				menuFrame.getSComp().getSButtonsPanel().getPreviewButton().setEnabled(false);
				menuFrame.getSComp().getSButtonsPanel().getEditPropertiesButton().setEnabled(false);
				menuFrame.getSComp().getSButtonsPanel().getDeleteButton().setEnabled(false);
				menuFrame.getSignLinks().getMenuComponent(0).setEnabled(true);
				menuFrame.getSignLinks().getMenuComponent(1).setEnabled(false);
				menuFrame.getSignLinks().getMenuComponent(2).setEnabled(false);

			}
			else
			{
				// print an error message
				System.out.println("Error. No signlink is selected");
			}
		}

		if (event.getActionCommand().equals("preview sign"))
		{
			int selected = menuFrame.getSComp().getSelected();
			// if (selected > -1)
			{
				vComp.getVideoButtonsPanel().getVSlide().setValue(menuFrame.getSComp().getSign(selected).getMStart());
				// changed setTime to setTimeMili
				vComp.getVideoPanel().setTimeMiliseconds(vComp.getVideoButtonsPanel().getVSlide().getValue());
				menuFrame.getSComp().getSign(selected).setPreviewed(true);
				System.out.println(vComp.getVideoButtonsPanel().getPlayPauseIcon());
				System.out.println(vComp.getVideoButtonsPanel().getPauseIcon());
				if (vComp.getVideoButtonsPanel().getPlayPauseIcon() == vComp.getVideoButtonsPanel().getPlayIcon())
					vComp.getVideoButtonsPanel().getPlayPauseButton().doClick();
			}
		}

	}

}
