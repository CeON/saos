var mainDir = '../src/main/webapp/WEB-INF/static/';


module.exports = function(grunt) {
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    gradleProp: (function() {
    	var lines = grunt.file.read('../../gradle.properties').split("\n"),
    		i = 0,
    		length = lines.length,
    		temp = "",
    		version = "";
    	
    	for (i = 0; i < length; i += 1) {
    		temp = lines[i].split("=");
    		if (temp[0] === "version") {
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
          banner: '/*! <%= pkg.name %> version <%= gradleProp %> <%= grunt.template.today("dd-mm-yyyy") %> */\n'
        },
        dist: {
          files: {
        	  '../src/main/webapp/WEB-INF/static/dist/saos.min.js' : '../src/main/webapp/WEB-INF/static/javascript/saos-common.js' /*['<%= concat.dist.dest %>']*/
          }
        }
    },
    
    /* Sass */
    
    sass: {
      dev: {
        options: {
          style: 'expanded',
          banner: '<%= tag.banner %>',
          compass: false
        },
        files: [{
         expand: true,
         cwd: mainDir + 'stylesheet/scss', //'<%= webapp.dir %>' + 'stylesheet/scss', //'<%= project.dev.sass %>',
         src: ['*.scss'],
         dest: mainDir + 'stylesheet/.generated', //'<%= webapp.dir %>' + 'stylesheet/.generated', //'<%= project.css %>', // '<%= project.dev.css %>',
         ext: '.css'
        }]
      },
      
      dist: {
        options: {
          style: 'compressed',
          banner: '<%= tag.banner %>',
          compass: false
        },
        files: [{
         expand: true,
         cwd: 'sass',
         src: ['*.scss', 'adapt/*.scss'],
         dest: 'css',
         ext: '.css'
        }]
      }
     },
     
     /*
     concat_css: {
         options: {
           // Task-specific options go here.
         },
         all: {
           src: ["css/base.css", "css/960.css", "css/style.css", "css/pon.css", "css/top_navigation.css"],
           dest: "dist/styles.min.css"
         },
     }*/
     
     
     log: {
    	 bar: 'hello'
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
  
  grunt.registerTask('build', ['uglify', 'sass:dev']);
 
  grunt.registerTask('wcag', ['accessibility']);
  grunt.registerTask('valid', ['validation']);
  
  grunt.registerTask('default', ['validation']);
};