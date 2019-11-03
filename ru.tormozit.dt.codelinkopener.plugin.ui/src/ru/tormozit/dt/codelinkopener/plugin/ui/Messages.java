
package ru.tormozit.dt.codelinkopener.plugin.ui;

import org.eclipse.osgi.util.NLS;

/**
 * Данный класс - представитель локализации механизма строк в Eclipse.
 */
class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = "ru.tormozit.dt.codelinkopener.plugin.ui.messages";

    public static String DataProcessingHandler_Error;
    public static String DataProcessingHandlerDialog_dialog_title;
    public static String DataProcessingHandlerDialog_dialog_message;
    public static String DataProcessingHandlerDialog_dialog_text;

    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages()
    {
    }
}
