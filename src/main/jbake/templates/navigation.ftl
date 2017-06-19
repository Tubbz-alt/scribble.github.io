	<!-- Fixed navbar -->
    <nav class="navbar navbar-fixed-top" role="navigation">
      <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
<!--
-->
        <div class="navbar-header">
          <button class="navbar-toggle collapsed" data-target="#bs-example-navbar-collapse-1" data-toggle="collapse" type="button">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        </div>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
          <ul class="nav navbar-nav">
            <li><a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>index.html">Home</a></li>
            <li>
              <a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>docs/overview/getting-started.html">Getting
                  Started</a>
            </li>
                <li class="dropdown">
                  <a aria-expanded="false" class="dropdown-toggle" data-toggle="dropdown" href="#" role="button">
                    Documentation<span class="caret"></span>
                  </a>
                  <ul class="dropdown-menu" role="menu">
                    <!--<li>
                      <a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>docs/user/user-guide.html">User Documentation</a>
                    </li>-->
										<li>
											<a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>docs/scribble-java.html#QUICK">Quick Start (Scribble-Java)</a>
										</li>
										<li>
											<a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>docs/scribble-java.html#SCRIBCORE">Language Reference</a>
										</li>
                    <li class="menu-item dropdown dropdown-submenu">
                      <a href="#" class="dropdown-toggle" data-toggle="dropdown">Java Tooling</a>
                      <ul class="dropdown-menu">
                        <li class="menu-item ">
                          <a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>docs/scribble-java.html">API Generation Tutorial</a>
                        </li>
                      </ul>
                    </li>
                    <li class="menu-item dropdown dropdown-submenu">
                      <a href="#" class="dropdown-toggle" data-toggle="dropdown">Scala Tooling</a>
                      <ul class="dropdown-menu">
                        <li class="menu-item ">
                          <a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>docs/scribble-scala.html">API Generation Tutorial and Demo Artifact</a>
                        </li>
                      </ul>
                    </li>
                  </ul>
                </li>

            <li class=""><a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>downloads.html">Downloads</a></li>
            <li class="dropdown"><a aria-expanded="false" class="dropdown-toggle" data-toggle="dropdown" href="#" role="button">
              Community
              <span class="caret"></span>
            </a>
            <ul class="dropdown-menu" role="menu">

              <li>
                <a href="https://groups.google.com/forum/#!forum/scribble-user">User Forum</a>
              </li>
              <li>
                <a href="https://groups.google.com/forum/#!forum/scribble-lang">Scribble Language Forum</a>
              </li>
              <li>
                <a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>community/issues.html">Reporting Issues</a>
              </li>
              <li>
                <a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>blog.html">Blog</a>
              </li>
              <li>
                <a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>community/collaborators.html">Collaborators</a>
              </li>
              <li>
                <a href="<#if (content.rootpath)??>${content.rootpath}<#else></#if>license.html">License</a>
              </li>
              <li>
                <a href="https://travis-ci.org/scribble">CI Builds</a>
              </li>
            </ul></li>
          </ul>
        </div>
      </div>
    </nav>
