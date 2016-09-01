(function (document, $) { // (1)
    'use strict';

    $(document).on('dialog-ready', function (e) { // (2)

        // a constant we use throughout
        var INHERIT = 'inherit';

        // our dialog!
        var $dialog = $('.cq-dialog-floating'); // (3)
        if(!$dialog) {
            console.error('Could not get the dialog');
            return;
        }

        var componentPath = $dialog.attr('action').replace('_jcr_content', 'jcr:content');
        var isPageProperties = endsWith(componentPath, 'jcr:content');

        // switch between page properties dialogs and component dialogs
        if(isPageProperties) { // (4)

            // (5)
            var resourceType = Granite.author.page.info.pageResourceType;
            var dialogPath = '/apps/' + resourceType + '/cq:dialog';

            getAndSetSizes(dialogPath, $dialog) // (5.1)
                .fail(function() { // if we fail, try the dialog of the resourcesupertype (5.2)
                    var componentPath = '/apps/' + resourceType;
                    $.getJSON(componentPath + '.json').done(function (data) {
                        var dialogPath = '/apps/' + data['sling:resourceSuperType'] + '/cq:dialog';
                        getAndSetSizes(dialogPath, $dialog);
                    });
                })

        } else {
            // (6)
            // the list of Editables (an Editable is an instance of a component on a Page)
            var editables, error = true;
            if(Granite.author) {
                editables = Granite.author.store;
                error = typeof editables === 'undefined';
            }

            if(error) {
                console.error('Could not fetch the Editables');
                return;
            }

            // loop over each editable and find the one that matches to the currently open dialog
            $.each(editables, function(index, value) {
                if(value.path == componentPath) { // (6.1)
                    // the path to the component dialog, which holds the width and height properties
                    var dialogPath = value.config.dialog;
                    getAndSetSizes(dialogPath, $dialog); // (6.2)
                    return false; // break out of $.each
                }
            });
        }

        /**
         * Gets the sizes from the properties at dialogPath, and sets those dimensions on the
         * $dialog element
         *
         * @param $dialog
         * @param dialogPath
         * @returns {*} the jqxhr object of the request
         */
        function getAndSetSizes(dialogPath, $dialog) { // (7)
            return $.getJSON( dialogPath + '.json')
                .done(function(data) {
                    setDialogSize($dialog, data.width, data.height);
                    addFullscreenToggle($dialog, data.width, data.height);
                });
        }

        /**
         * Sets the width and the height on the dialog, the dialog's content element(s), and re-centers
         * the dialog.
         *
         * @param $dialog
         * @param width
         * @param height
         */
        function setDialogSize($dialog, width, height) { // (8)

            if(width) {

                // set the width!
                $dialog.css('width', width);

                var smallerWidth = parseInt(width) - 5;
                var matches = width.match(/[a-zA-z]+/);
                if(matches) {
                    var unit = matches[0];
                    $dialog.find('.coral-FixedColumn > .coral-FixedColumn-column').css('width', smallerWidth + unit);
                } else if(width === INHERIT) {
                    $dialog.find('.coral-FixedColumn > .coral-FixedColumn-column').css('width', width);
                }
            }

            if(height) {

                // For fixed column layouts
                $dialog.find('.coral-FixedColumn').css('height', height);

                // For tabbed layouts
                $dialog.find('.coral-TabPanel .coral-TabPanel-content').css('height', height);

                // add more layouts here!
            }


            /*if(height) {

                var smallerHeight = parseInt(height) - 8;
                
                // For fixed column layouts
                
                $dialog.find('.coral-FixedColumn').css('height', smallerHeight + unit);
                
                // For tabbed layouts
                
                $dialog.find('.coral-TabPanel .coral-TabPanel-content').css('height', smallerHeight + unit);
                
                // add more layouts here!
*/



            // force repositioning of the dialog by triggering this event, see
            // /libs/cq/gui/components/authoring/clientlibs/editor/js/DialogFrame.js
            $(document).trigger('cq-sidepanel-resized.dialogframe');
        }

        /**
         * When switching back/to fullscreen in the dialog, toggle between the custom and inherited
         * dimensions on the $dialog.
         *
         * @param $dialog
         * @param width
         * @param height
         */
        function addFullscreenToggle($dialog, width, height) { // (9)
            //
            $(document).on('click', '.cq-dialog-layouttoggle', function(e) {
                e.preventDefault();
                if($dialog.hasClass('cq-dialog-fullscreen')) {
                    setDialogSize($dialog, INHERIT, INHERIT);
                } else {
                    setDialogSize($dialog, width, height);
                }
            });
        }

        /**
         * Tests if a string ends with another string
         *
         * @param str
         * @param suffix
         * @returns {boolean}
         */
        function endsWith(str, suffix) {
            return str.indexOf(suffix, str.length - suffix.length) !== -1;
        }
    });

})(document, Granite.$);