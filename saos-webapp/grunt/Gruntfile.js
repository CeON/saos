var mainDir = '../src/main/webapp/WEB-INF/static/';


module.exports = function(grunt) {
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    gradleProp: (function() {
    	var lines = grunt.file.read('../src/main/resources/saos.version.properties').split("\n"),
    		i = 0,
    		length = lines.length,
    		temp = "",
    		version = "";
    	
    	for (i = 0; i < length; i += 1) {
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
        css: [
    	      '<%= webapp.dir %>stylesheet/css'
        ],
        
        sass: [
    		  '<%= webapp.dir %>' + 'stylesheet/scss'
         ],
        
        dev: {
        	generated: [
        	      mainDir + 'stylesheet/.generated'
            ],
        	css: [
        	      '<%= webapp.dir %>' + 'stylesheet/css'
            ],
            sass: [
           		  '<%= webapp.dir %>' + 'stylesheet/scss'
            ],
            cssmin: [
                     'dist/style.min.css'
            ],
            js: [
              'javascript**/*.js'
            ]
        },
        
    },
    
    tag: {
        banner: '/*\n' +
            ' * <%= pkg.name %>\n' +
            ' * <%= pkg.title %>\n' +
            ' * <%= pkg.url %>\n' +
            ' * @author <%= pkg.author %>\n' +
            ' * @version <%= gradleProp %>\n' +
            ' * Copyright <%= pkg.copyright %>. <%= pkg.license %> licensed.\n' +
            ' */\n'
    },
    
    validation: {
        options: {
        	charset: 'utf-8',
            doctype: 'HTML5',
            failHard: true,
            reset: true,
            remotePath: 'http://localhost:8080/saos-webapp',
            remoteFiles: ['/search', '/results'],
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
    
    /*concat: {
        options: {
          separator: ';'
        },
        dist: {
          src: ['statics/javascripts/prime.js'],
          dest: 'dist/prime.js'
        }
    },*/
    
    uglify: {
        options: {
          banner: '/* <%= pkg.name %> version <%= gradleProp %> <%= grunt.template.today("dd-mm-yyyy") %> */\n'
        },
        dist: {
          files: {
        	  '../src/main/webapp/WEB-INF/static/javascript/.generated/saos.min.js' : '../src/main/webapp/WEB-INF/static/javascript/saos-common.js' /*['<%= concat.dist.dest %>']*/
          }
        }
    },
 
	concat_css: {
	    options: {
		     },
	    all: {
	      src: ['<%= project.dev.generated %>/modern.css', '<%= project.dev.generated %>' + '/styles.css'],
	      dest: '<%= project.dev.generated %>' + '/dist/styles.min.css'
	    }
	},
	
	usebanner: {
	  options: {
	    position: 'top',
	    banner: '/* <%= pkg.name %> version <%= gradleProp %> <%= grunt.template.today("dd-mm-yyyy") %> */\n'
	  },
	  files: {
	    src: [
          	'<%= project.dev.generated %>/dist/styles.min.css'
	    ]
	  }
	},
     
    watch: {
         sass: {
           files: '<%= project.dev.sass %>' + '/{,*/}*.{scss,sass}',
           tasks: ['sass:dev']
         }
     }
     
  });
  
  
  require('matchdep').filterDev('grunt-*').forEach(grunt.loadNpmTasks);

  //grunt.registerTask('watch', ['watch']);
  
  grunt.registerTask('gradleProp','Show version of project saos', function() {
	  grunt.log.writeln(grunt.config('gradleProp'));
  });
  
  grunt.registerTask('build', ['uglify', 'concat_css', 'usebanner']);
 
  grunt.registerTask('wcag', ['accessibility']);
  grunt.registerTask('valid', ['validation']);
  
  grunt.registerTask('default', ['validation']);
};