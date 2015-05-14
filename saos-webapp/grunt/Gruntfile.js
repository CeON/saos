var mainDir = '../src/main/webapp/WEB-INF/static/';


module.exports = function(grunt) {
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    gradleProp: (function() {
    	var lines = grunt.file.read('../src/main/resources/saos.version.properties').split("\n"),
    		length = lines.length,
    		temp = "",
    		version = "";
    	
    	for (var i = 0; i < length; i += 1) {
    		temp = lines[i].split("=");
    		if (temp[0] === "saos.version") {
    			version = temp[1];
    		}
    	}
    	
    	return version;
    })(),
    
    webapp: {
    	dir: 'static/'
    },
    
    project: {
        app: 'app',

        sass: [
    		  '<%= webapp.dir %>' + 'stylesheet/scss'
        ],
        
        css: {
        	dir: mainDir + 'stylesheet/css',
    		generated: mainDir + 'stylesheet/.generated',
        	distFile: mainDir + 'stylesheet/.generated/saos.css',
    		distFileMin:  mainDir + 'stylesheet/.generated/saos.min.css',
        },
        
        sass: {
        	dir: mainDir + 'stylesheet/sass',
        },
        
        js: {
        	dir: mainDir + 'javascript',
        	generated: mainDir + 'javascript/.generated',
        	distFile: mainDir + 'javascript/.generated/saos.js',
        	distFileMin: mainDir + 'javascript/.generated/saos.min.js',
        },
        
    },
    
    tag: {
        banner: '/*\n' +
            ' * <%= pkg.name %>\n' +
            ' * <%= pkg.title %>\n' +
            ' * \n' +
            ' * <%= grunt.template.today("dd-mm-yyyy") %>\n' +
            ' * @author <%= pkg.author %>\n' +
            ' * @version <%= gradleProp %>\n' +
            //' * Copyright <%= pkg.copyright %>. <%= pkg.license %> licensed.\n' +
            ' */\n'
    },
    
    validation: {
        options: {
        	charset: 'utf-8',
            doctype: 'HTML5',
            failHard: true,
            reset: true,
            remotePath: 'http://localhost:8080/saos-webapp',
            remoteFiles: ['/search', '/', '/judgments/176262', '/siteMap', '/analysis'],
            relaxerror: [
                         'Bad value X-UA-Compatible for attribute http-equiv on element meta.',
                         'Element img is missing required attribute src.'
                       ],
            path: 'validation/validation-status.json',
            reportpath: 'validation/validation-report.json'
        },
        files: {
            src: '../src/main/webapp/WEB-INF/view/*.jsp'
        }
    },
    
    accessibility: {
	  options : {
	    accessibilityLevel: 'WCAG2A',
	    force: true,
	    domElement: true
	  },
	  test : {
	    files: [{
	      expand  : true,
	      cwd     : '../src/main/webapp/WEB-INF/view/',
	      src     : ['*.jsp'],
	      dest    : 'wcag/',
	      ext     : 'report.txt'
	    }]
	  }
	},
    
	//------------------------ JAVASCRIPT MODULES --------------------------
	
    concat: {
        options: {
        	separator: ';',
        	stripBanners: false,
        },
        dist: {
        	src: [
				  '<%= project.js.dir %>/libs/jquery-2.1.0.min.js',
				  '<%= project.js.dir %>/libs/jquery-ui-1.10.4.custom.min.js',
				  '<%= project.js.dir %>/libs/jquery.ui.datepicker-pl.js',
				  '<%= project.js.dir %>/libs/jquery.ui.datepicker-en.js',
				  '<%= project.js.dir %>/libs/jquery.form.js',
				  '<%= project.js.dir %>/libs/jquery.blockUI.js',
				  '<%= project.js.dir %>/libs/jquery.clipboard/jquery.clipboard.js',
				  '<%= project.js.dir %>/libs/jquery.pseudo.js',
				  '<%= project.js.dir %>/libs/jquery.visible.min.js',
				  '<%= project.js.dir %>/libs/jquery.validate.min.js',
				  '<%= project.js.dir %>/libs/jquery.wrapInTag.js',
				  '<%= project.js.dir %>/libs/modernizr.js',
				  '<%= project.js.dir %>/libs/moment.min.js',
                  '<%= project.js.dir %>/libs/bootstrap.min.js',
				  '<%= project.js.dir %>/libs/jquery-dateFormat.min.js',
				  
				  '<%= project.js.dir %>/libs/jquery.flot.min.js',
				  '<%= project.js.dir %>/libs/jquery.flot.navigate.min.js',
				  '<%= project.js.dir %>/libs/jquery.flot.resize.min.js',
				  '<%= project.js.dir %>/libs/jquery.flot.selection.min.js',
                  '<%= project.js.dir %>/libs/jquery.flot.orderBars.min.js',
				  '<%= project.js.dir %>/libs/jquery.flot.stack.min.js',
				  '<%= project.js.dir %>/libs/jquery.flot.time.min.js',
				  
				  '<%= project.js.dir %>/modules/flotSupport.js',
				  
				  
				  '<%= project.js.dir %>/modules/typography.js',
        	      '<%= project.js.dir %>/modules/cookies.js',
        	      '<%= project.js.dir %>/modules/dateFormat.js',
        	      '<%= project.js.dir %>/modules/courtDivisionSelect.js',
        	      '<%= project.js.dir %>/modules/infoFormSection.js',
        	      '<%= project.js.dir %>/modules/infoFormSectionExtractFunctions.js',
        	      '<%= project.js.dir %>/modules/search/clearSearchForm.js',
        	      '<%= project.js.dir %>/modules/search/searchFormMode.js',
        	      '<%= project.js.dir %>/modules/search/searchCriteria.js',
        	      '<%= project.js.dir %>/modules/search/searchSettingsToolTip.js',
        	      '<%= project.js.dir %>/modules/search/searchContext.js',
        	      '<%= project.js.dir %>/modules/search/searchFilters.js',
        	      '<%= project.js.dir %>/modules/search/cleanUrlForm.js',
        	      '<%= project.js.dir %>/modules/search/courtCriteriaForm.js',
        	      '<%= project.js.dir %>/modules/suggester.js',
        	      '<%= project.js.dir %>/modules/cookiePolicy.js',
        	      '<%= project.js.dir %>/modules/addOptionsToSelect.js',
        	      '<%= project.js.dir %>/modules/details/infoBox.js',
        	      '<%= project.js.dir %>/judgmentDetails.js',
        	      '<%= project.js.dir %>/judgmentSearch.js',
        	      '<%= project.js.dir %>/analysis.js',
        	      '<%= project.js.dir %>/saos-common.js'
    	      ],
    	      
        	dest: '<%= project.js.distFile %>',
        }
    },
    
    uglify: {
        options: {
        },
        dist: {
          files: {
        	  '<%= project.js.distFileMin %>' : '<%= project.js.distFile %>'
          }
        }
    },
    
    jshint: {
    	all: ['<%= project.js.dir %>/modules/*.js']
	},
	
	jscs: {
        all: {
        	options: {
                "standard": "Jquery"
            },
            files: {
                src: ['<%= project.js.dir %>/modules/*.js'] 
            }
        }
    },

	//------------------------ CSS MODULES --------------------------
    
    autoprefixer: {
        dist: {
            files: {
            	'<%= project.css.generated %>/styles.css' : '<%= project.css.generated %>/styles.css'
            }
        }
    },
    
	concat_css: {
	    options: {
		     },
	    all: {
	      src: ['<%= project.css.dir %>/*/*.min.css', '<%= project.css.dir %>/*.min.css', '<%= project.css.generated %>/styles.css'],
	      dest: '<%= project.css.distFile %>'
	    }
	},
	
	cssmin: {
		 options: {
			 compatibility: 'ie8',
			 keepSpecialComments: '*',
			 noAdvanced: true
		 },
		 core: {
			 src: '<%= project.css.distFile %>',
			 dest: '<%= project.css.distFileMin %>'
		 },
	},
	 
	csslint: {
		  strict: {
		    options: {
		      import: 2
		    },
		    src: ['<%= project.css.generated %>/styles.css']
		  },
		  lax: {
		    options: {
		      import: false
		    },
		    src: ['<%= project.css.generated %>/styles.css']
		  }
	},
	 
	 
	//------------------------ MISC MODULES --------------------------
	
	usebanner: {
	  options: {
	    position: 'top',
    	banner: '<%= tag.banner %>'
	  },
	  files: {
	    src: [
          	'<%= project.css.distFileMin %>',
          	'<%= project.js.distFileMin %>'
	    ]
	  }
	},
     
    watch: {
         sass: {
           files: '<%= project.sass.dir %>' + '/{,*/}*.{scss,sass}',
           tasks: ['build']
         }
     }
     
  });
  
  
  require('matchdep').filterDev('grunt-*').forEach(grunt.loadNpmTasks);

  //grunt.registerTask('watch', ['watch']);
  
  grunt.registerTask('gradleProp','Show version of project saos', function() {
	  grunt.log.writeln(grunt.config('gradleProp'));
  });
  
  grunt.registerTask('doc:jshint', ['jshint:all']);
  grunt.registerTask('doc:jscs', ['jscs']);
  
  grunt.registerTask('doc:csslint', ['csslint:strict']);
  
  grunt.registerTask('build-css', ['autoprefixer', 'concat_css', 'cssmin:core']);
  
  grunt.registerTask('build-js', ['concat', 'uglify']);
  
  grunt.registerTask('build', ['build-js', 'build-css', 'usebanner']);
 
  grunt.registerTask('wcag', ['accessibility']);
  grunt.registerTask('valid', ['validation']);
  
  grunt.registerTask('default', ['validation']);

};
